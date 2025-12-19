package mir.oslav.moaihead.ui

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mir.oslav.moaihead.data.MetadataRepo
import mir.oslav.moaihead.utils.tryGetConnectedPhone
import moaihead.data.Endpoints
import moaihead.data.model.Mood
import moaihead.data.model.MoodEntry
import moaihead.data.model.PlainMoodEntry
import moaihead.room.LocalDatabaseRepo
import javax.inject.Inject


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@HiltViewModel
class NewEntryViewModel @Inject constructor(
    val repo: LocalDatabaseRepo,
    val metadataRepo: MetadataRepo,
) : ViewModel() {


    private val mNotesByMood: MutableStateFlow<List<Pair<String, Int>>> =
        MutableStateFlow(value = emptyList())
    val notesByMood: StateFlow<List<Pair<String, Int>>> = mNotesByMood.asStateFlow()


    fun loadNotesForMood(mood: Mood) {
        viewModelScope.launch {
            val list = metadataRepo.notesMetadata.value[mood] ?: emptyList()
            mNotesByMood.value = list.sortedByDescending(selector = Pair<String, Int>::second)
        }
    }


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
        val msgClient = Wearable.getMessageClient(activity)

        viewModelScope.launch {
            val phoneNode = activity.tryGetConnectedPhone()
            if (phoneNode != null) {
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
            } else {
                callback(false)
            }
        }
    }
}