package mir.oslav.moaihead.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import moaihead.data.DataSourceRepository
import moaihead.data.model.MoodEntry
import javax.inject.Inject


/**
 * @author Miroslav Hýbler <br>
 * created on 02.12.2025
 */
@HiltViewModel
class EntryViewModel @Inject constructor(
    private val firestoreRepo: DataSourceRepository,
) : ViewModel() {


    fun updateNote(
        note: String?,
        entry: MoodEntry
    ) {
        viewModelScope.launch {
            firestoreRepo.insertOrUpdateMood(
                entry = entry.copy(
                    note = note
                        ?.takeIf(predicate = String::isNotBlank)
                )
            )
        }
    }


    fun delete(entry: MoodEntry) {
        viewModelScope.launch {
            firestoreRepo.deleteMood(entry = entry)
        }
    }

}