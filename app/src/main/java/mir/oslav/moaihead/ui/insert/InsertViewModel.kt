package mir.oslav.moaihead.ui.insert

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mir.oslav.moaihead.PhoneWearOsListenerService
import mir.oslav.moaihead.ui.entry.EntryViewModel
import moaihead.data.BaseDataSourceRepository
import moaihead.data.model.Mood
import moaihead.data.model.PlainMoodEntry
import moaihead.ui.BaseEntryViewModel
import javax.inject.Inject


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
@HiltViewModel
class InsertViewModel @Inject constructor(
    repo: BaseDataSourceRepository,
    @ApplicationContext
    private val context: Context,
) : BaseEntryViewModel(
    repo = repo,
) {

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
            //Updating metadata for WearOS after new entry
            PhoneWearOsListenerService.onMetadataSyncRequested(
                context = context,
                repo = repo,
            )
        }
    }
}