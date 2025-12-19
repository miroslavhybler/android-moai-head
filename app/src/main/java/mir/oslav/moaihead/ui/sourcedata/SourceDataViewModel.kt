package mir.oslav.moaihead.ui.sourcedata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import moaihead.data.BaseDataSourceRepository
import moaihead.data.LocalToRemoteSyncer
import moaihead.data.model.SyncState
import javax.inject.Inject

/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
@HiltViewModel
class SourceDataViewModel @Inject constructor(
    val repo: BaseDataSourceRepository,
    private val syncer: LocalToRemoteSyncer,
) : ViewModel() {

    val syncState: StateFlow<SyncState> = syncer.syncState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SyncState.Unknown
    )

    fun requestLocalToRemoteSync() {
        syncer.requestSync()
    }
}