package mir.oslav.moaihead.ui.insert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import moaihead.data.DataSourceRepository
import moaihead.data.model.Mood
import moaihead.data.model.PlainMoodEntry
import javax.inject.Inject


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
@HiltViewModel
class InsertViewModel @Inject constructor(
    val repo: DataSourceRepository,
) : ViewModel() {


    fun insert(
        mood: Mood,
        note: String?,
    ) {
        viewModelScope.launch{
            repo.insertOrUpdateMood(
                entry = PlainMoodEntry(
                    mood = mood.value,
                    note = note,
                    timestamp = System.currentTimeMillis(),
                    source = 1,
                )
            )
        }
    }
}