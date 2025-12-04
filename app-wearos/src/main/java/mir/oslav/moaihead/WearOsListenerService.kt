package mir.oslav.moaihead

import android.util.Log
import androidx.compose.ui.util.fastForEach
import com.google.android.gms.tasks.Task
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
import kotlinx.coroutines.suspendCancellableCoroutine
import moaihead.data.Endpoints
import moaihead.data.model.PlainMoodEntry
import moaihead.room.LocalDatabaseRepo
import moaihead.room.MoodEntity
import javax.inject.Inject
import kotlin.coroutines.resume


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
    lateinit var repo: LocalDatabaseRepo


    override fun onMessageReceived(event: MessageEvent) {
        super.onMessageReceived(event)
        when (event.path) {
            Endpoints.FromPhoneToWear.REQUEST_SYNC -> {
                coroutineScope.launch {
                    syncData()
                }
            }

            else -> {
                Log.e("WearOsListenerService", "(WearOS) Unknown path: ${event.path}")
            }
        }
    }


    private suspend fun syncData() {
        val list = repo.getAllNotUploaded()
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
            repo.insertOrUpdateMood(entry = updatedEntity)
        }
    }


    suspend fun <T> Task<T>.awaitResult(): Result<T> =
        suspendCancellableCoroutine { cont ->
            addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cont.resume(value = Result.success(value = task.result))
                } else {
                    cont.resume(
                        value = Result.failure(
                            exception = task.exception
                                ?: Exception("Task failed")
                        )
                    )
                }
            }
        }

}