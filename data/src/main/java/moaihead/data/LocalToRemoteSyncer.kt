package moaihead.data

import kotlinx.coroutines.flow.Flow
import moaihead.data.model.SyncState


/**
 * @author Miroslav Hýbler <br>
 * created on 09.12.2025
 */
interface LocalToRemoteSyncer {
    val syncState: Flow<SyncState>
    fun requestSync()
}