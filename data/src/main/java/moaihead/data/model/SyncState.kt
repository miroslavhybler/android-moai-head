package moaihead.data.model

/**
 * @author Miroslav Hýbler <br>
 * created on 09.12.2025
 */
sealed interface SyncState {
    data object Unknown : SyncState
    data object SyncRunning : SyncState
    data class SyncFinished constructor(val isSuccess: Boolean) : SyncState
}