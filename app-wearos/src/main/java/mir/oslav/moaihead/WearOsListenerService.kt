package mir.oslav.moaihead

import android.util.Log
import androidx.compose.ui.util.fastForEach
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import mir.oslav.moaihead.data.MetadataRepo
import moaihead.data.Endpoints
import moaihead.data.model.PlainMoodEntry
import moaihead.data.utils.awaitResult
import moaihead.room.LocalDatabaseRepo
import moaihead.room.MoodEntity
import javax.inject.Inject


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@AndroidEntryPoint
class WearOsListenerService : WearableListenerService() {

    private val coroutineScope = CoroutineScope(
        context = Dispatchers.IO
            .plus(context = CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
            })
            .plus(context = CoroutineName(name = "WearOsListenerService"))
    )


    @Inject
    lateinit var databaseRepo: LocalDatabaseRepo

    @Inject
    lateinit var metadataRepo: MetadataRepo

    override fun onMessageReceived(event: MessageEvent) {
        super.onMessageReceived(event)
        when (event.path) {
            Endpoints.FromPhoneToWear.REQUEST_WEAR_SYNC -> {
                coroutineScope.launch {
                    sendDataFromWearToPhone()
                }
            }

            Endpoints.FromPhoneToWear.REQUEST_METADATA_SYNC_RESPONSE -> {
                metadataRepo.saveMetadata(
                    metadata = Json.decodeFromString(string = String(bytes = event.data))
                )
            }

            Endpoints.FromPhoneToWear.REQUEST_MOOD_AND_NOTES_RESPONSE -> {
                metadataRepo.saveNotes(
                    notes = Json.decodeFromString(string = String(bytes = event.data))
                )
            }

            else -> {
                Log.e("WearOsListenerService", "(WearOS) Unknown path: ${event.path}")
            }
        }
    }


    private suspend fun sendDataFromWearToPhone() {
        val list = databaseRepo.loadAllNotSynced()
        val nodeClient = Wearable.getNodeClient(this)
        val msgClient = Wearable.getMessageClient(this)

        nodeClient.connectedNodes.awaitResult()
            .getOrNull()
            ?.firstOrNull()?.let { phoneNode ->
                list.fastForEach {
                    syncItem(entity = it, node = phoneNode, msgClient = msgClient)
                }
            }

    }


    private suspend fun syncItem(
        entity: MoodEntity,
        node: Node,
        msgClient: MessageClient,
    ) {
        val result = msgClient.sendMessage(
            node.id,
            Endpoints.FromWearToPhone.INSERT_MOOD_ENTRY,
            PlainMoodEntry.Serializer.encode(entry = entity.toPlainMoodEntry())
        ).awaitResult().getOrNull()

        if (result != null) {
            //Sync successful
            val updatedEntity = entity.copy(isSynchronized = true)
            databaseRepo.insertOrUpdateMood(entry = updatedEntity)
        }
    }
}