package mir.oslav.moaihead.ui

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import moaihead.data.DataSourceRepository
import moaihead.data.Endpoints
import moaihead.data.model.MoodEntry
import moaihead.data.model.PlainMoodEntry
import javax.inject.Inject


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@HiltViewModel
class NewEntryViewModel @Inject constructor(
    val repo: DataSourceRepository,
) : ViewModel() {


    fun insert(
        moodEntry: MoodEntry,
        activity: Activity,
        onFinish: () -> Unit,
    ) {

        trySendToPhone(
            entry = moodEntry.toPlain(),
            activity = activity,
            callback = { phoneSendResult ->
                if (phoneSendResult == null) {
                    //Phone unavailable, saving locally
                    viewModelScope.launch {
                        repo.insertOrUpdateMood(entry = moodEntry)
                    }
                    Toast.makeText(
                        activity,
                        "Saving locally",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        if (phoneSendResult) "Success" else "Failure",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                onFinish()
            }
        )

        viewModelScope.launch {
            repo.insertOrUpdateMood(entry = moodEntry)
        }
    }



    private fun trySendToPhone(
        entry: PlainMoodEntry,
        activity: Activity,
        callback: (Boolean?) -> Unit,
    ) {
        val nodeClient = Wearable.getNodeClient(activity)
        val msgClient = Wearable.getMessageClient(activity)

        // Find connected phone
        nodeClient.connectedNodes.addOnSuccessListener { nodes ->
            if (nodes.isEmpty()) {
                callback(null)
                Toast.makeText(
                    activity,
                    "No phone connected",
                    Toast.LENGTH_SHORT,
                ).show()
                return@addOnSuccessListener
            }

            val phoneNode = nodes.firstOrNull() ?: return@addOnSuccessListener
            msgClient.sendMessage(
                phoneNode.id,
                Endpoints.FromWearToPhone.INSERT_MOOD_ENTRY,
                PlainMoodEntry.Serializer.encode(entry = entry)
            ).addOnSuccessListener {
                callback(true)
            }.addOnFailureListener { exception ->
                exception.printStackTrace()
                callback(false)
            }

        }.addOnFailureListener { exception ->
            exception.printStackTrace()
            callback(false)
        }
    }
}