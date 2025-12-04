package mir.oslav.moaihead

import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import moaihead.data.DataSourceRepository
import moaihead.data.PlainMoodEntry
import javax.inject.Inject


/**
 * @author Miroslav Hýbler <br>
 * created on 01.12.2025
 */
@AndroidEntryPoint
class WearOsListenerService : WearableListenerService() {

    @Inject
    lateinit var repo: DataSourceRepository

    override fun onCreate() {
        super.onCreate()
    }


    override fun onMessageReceived(event: MessageEvent) {
        super.onMessageReceived(event)
        val entry = PlainMoodEntry.Serializer.decode(bytes = event.data)
        repo.insertOrUpdateMood(entry = entry)
    }

    override fun onDataChanged(p0: DataEventBuffer) {
        super.onDataChanged(p0)

        p0.map { it.dataItem.uri }
            .forEach { uri ->
                // Get the node ID from the host value of the URI.
                val nodeId: String? = uri.host
                // Set the data of the message to be the bytes of the URI.
                val payload: ByteArray = uri.toString().toByteArray()

                if (nodeId=="/moai/mood_entry") {
                    val entry = PlainMoodEntry.Serializer.decode(bytes = payload)
                    repo.insertOrUpdateMood(entry = entry)
                }
            }
    }
}