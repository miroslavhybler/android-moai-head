package mir.oslav.moaihead.ui.insert

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import moaihead.data.Mood
import moaihead.data.PlainMoodRecord
import moaihead.firestore.FirestoreRepo
import javax.inject.Inject


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
@HiltViewModel
class InsertViewModel @Inject constructor(
    val repo: FirestoreRepo,
) : ViewModel() {


    fun insert(
        mood: Mood,
        note: String?,
    ) {
        repo.insertMood(
            plainMoodRecord = PlainMoodRecord(
                mood = mood.value,
                note = note,
                timestamp = System.currentTimeMillis(),
                source = 1,
            )
        )
    }
}