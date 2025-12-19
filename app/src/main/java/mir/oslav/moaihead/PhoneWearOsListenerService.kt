package mir.oslav.moaihead

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import mir.oslav.moaihead.utils.tryGetConnectedWearOS
import moaihead.data.BaseDataSourceRepository
import moaihead.data.Endpoints
import moaihead.data.model.AppMetadata
import moaihead.data.model.Mood
import moaihead.data.model.PlainMoodEntry
import moaihead.data.utils.awaitResult
import javax.inject.Inject
import kotlin.math.roundToInt


/**
 * @author Miroslav Hýbler <br>
 * created on 01.12.2025
 */
@AndroidEntryPoint
class PhoneWearOsListenerService : WearableListenerService() {

    @Inject
    lateinit var repo: BaseDataSourceRepository

    private val coroutineScope = CoroutineScope(
        context = Dispatchers.IO
            .plus(context = CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
            })
            .plus(context = CoroutineName(name = "WearOsListenerService"))
    )

    override fun onCreate() {
        super.onCreate()
    }


    override fun onMessageReceived(event: MessageEvent) {
        super.onMessageReceived(event)

        when (event.path) {
            Endpoints.FromWearToPhone.INSERT_MOOD_ENTRY -> {
                coroutineScope.launch {
                    val entry = PlainMoodEntry.Serializer.decode(bytes = event.data)
                    repo.insertOrUpdateMood(entry = entry)
                }
            }

            Endpoints.FromWearToPhone.REQUEST_METADATA_SYNC -> {
                coroutineScope.launch {
                    onMetadataSyncRequested()
                }
            }

            Endpoints.FromWearToPhone.REQUEST_MOOD_AND_NOTES -> {
                coroutineScope.launch {
                    val data = repo.groupModeNotes()
                    val messageClient = Wearable.getMessageClient(this@PhoneWearOsListenerService)
                    val wearOsNode = tryGetConnectedWearOS()
                    if (wearOsNode != null) {
                        messageClient.sendMessage(
                            wearOsNode.id,
                            Endpoints.FromPhoneToWear.REQUEST_MOOD_AND_NOTES_RESPONSE,
                            Json.encodeToString(value = data).toByteArray(),
                        ).awaitResult()
                    }
                }
            }

            else -> {
                Log.e("WearOsListenerService", "(Phone) Unknown path: ${event.path}")
            }
        }
    }



    private suspend fun onMetadataSyncRequested() {
        val average = repo.getTotalAverageMood()
        val averageInt = average.roundToInt()
        val mood = Mood.entries.find(predicate = { mood -> mood.value == averageInt })

        val metadata = AppMetadata(
            totalAverageMoodValue = average,
            totalAverageMood = mood,
        )

        val messageClient = Wearable.getMessageClient(this@PhoneWearOsListenerService)
        val wearOsNode = tryGetConnectedWearOS()

        if (wearOsNode != null) {
            messageClient.sendMessage(
                wearOsNode.id,
                Endpoints.FromPhoneToWear.REQUEST_METADATA_SYNC_RESPONSE,
                Json.encodeToString(value = metadata).toByteArray(),
            ).awaitResult()
        }
    }
}