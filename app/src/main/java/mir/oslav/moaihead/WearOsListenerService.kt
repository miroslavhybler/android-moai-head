package mir.oslav.moaihead

import android.util.Log
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moaihead.data.DataSourceRepository
import moaihead.data.Endpoints
import moaihead.data.model.PlainMoodEntry
import javax.inject.Inject


/**
 * @author Miroslav Hýbler <br>
 * created on 01.12.2025
 */
@AndroidEntryPoint
class WearOsListenerService : WearableListenerService() {

    @Inject
    lateinit var repo: DataSourceRepository

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
                val entry = PlainMoodEntry.Serializer.decode(bytes = event.data)
                coroutineScope.launch {
                    repo.insertOrUpdateMood(entry = entry)
                }
            }

            else -> {
                Log.e("WearOsListenerService", "(Phone) Unknown path: ${event.path}")
            }
        }
    }
}