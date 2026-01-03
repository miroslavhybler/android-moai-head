package mir.oslav.moaihead.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moaihead.data.BaseDataSourceRepository
import moaihead.data.model.Mood
import moaihead.data.model.MoodEntry
import moaihead.ui.BaseEntryViewModel
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2


/**
 * @author Miroslav Hýbler <br>
 * created on 02.12.2025
 */
@HiltViewModel
class EntryViewModel @Inject constructor(
    repo: BaseDataSourceRepository,
) : BaseEntryViewModel(
    repo = repo,
) {


    fun updateNote(
        note: String?,
        entry: MoodEntry
    ) {
        viewModelScope.launch {
            repo.insertOrUpdateMood(
                entry = entry.copy(
                    note = note
                        ?.takeIf(predicate = String::isNotBlank)
                )
            )
        }
    }


    fun delete(entry: MoodEntry) {
        viewModelScope.launch {
            repo.deleteMood(entry = entry)
        }
    }

}