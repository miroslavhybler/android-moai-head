package mir.oslav.moaihead.ui.insert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moaihead.data.BaseDataSourceRepository
import moaihead.data.model.Mood
import moaihead.data.model.PlainMoodEntry
import javax.inject.Inject


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
@HiltViewModel
class InsertViewModel @Inject constructor(
    val repo: BaseDataSourceRepository,
) : ViewModel() {


    private val mNotesByMood: MutableStateFlow<List<Pair<String, Int>>> =
        MutableStateFlow(value = emptyList())
    val notesByMood: StateFlow<List<Pair<String, Int>>> = mNotesByMood.asStateFlow()


    fun loadNotesForMood(mood: Mood) {
        viewModelScope.launch {
            val filtered = repo.moodData.value
                .filter(predicate = { it.mood == mood })
                .filter(predicate = { it.note != null })

            val grouped = filtered.groupBy(keySelector = { it.note!! })

            mNotesByMood.value = grouped
                .map(transform = { (note, entries) -> note to entries.size })
                .sortedByDescending(selector = { it.second })

        }
    }


    fun insert(
        mood: Mood,
        note: String?,
    ) {
        viewModelScope.launch {
            repo.insertOrUpdateMood(
                entry = PlainMoodEntry(
                    mood = mood.value,
                    note = note?.trim(),
                    timestamp = System.currentTimeMillis(),
                    source = 1,
                )
            )
        }
    }
}