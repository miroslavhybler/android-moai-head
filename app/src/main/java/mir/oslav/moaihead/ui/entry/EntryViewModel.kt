package mir.oslav.moaihead.ui.entry

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import moaihead.data.MoodEntry
import moaihead.firestore.FirestoreRepo
import javax.inject.Inject


/**
 * @author Miroslav Hýbler <br>
 * created on 02.12.2025
 */
@HiltViewModel
class EntryViewModel @Inject constructor(
    private val firestoreRepo: FirestoreRepo,
) : ViewModel() {


    fun updateNote(
        note: String?,
        entry: MoodEntry
    ) {
        firestoreRepo.insertOrUpdateMood(
            entry = entry.copy(
                note = note
                    ?.takeIf(predicate = String::isNotBlank)
            )
        )
    }



    fun delete(entry: MoodEntry) {
        firestoreRepo.deleteMood(entry = entry)
    }

}