@file:Suppress("RedundantVisibilityModifier")

package moaihead.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moaihead.data.BaseDataSourceRepository
import moaihead.data.model.Mood


/**
 * @author Miroslav Hýbler <br>
 * created on 02.01.2026
 */
public abstract class BaseEntryViewModel public constructor(
    protected val repo: BaseDataSourceRepository,
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

}