package moaihead.firestore

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import moaihead.room.LocalDatabaseRepo
import moaihead.room.MoodEntity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Miroslav Hýbler <br>
 * created on 08.12.2025
 */
@Singleton
class LocalToRemoteSyncRepo @Inject constructor(
    private val localRepo: LocalDatabaseRepo,
    private val firestoreRepo: FirestoreRepo,
) {

    sealed class State private constructor() {

        data object Unknown : State()

        data object SyncRunning : State()

        data class SyncFinished constructor(
            val isSuccess: Boolean,
        ) : State()
    }


    private val mSyncState: MutableStateFlow<State> = MutableStateFlow(value = State.Unknown)
    val syncState: StateFlow<State> = mSyncState.asStateFlow()


    suspend fun tryRequestSync() {
        if (syncState.value is State.SyncRunning) {
            return
        }

        val notSynced = localRepo.loadAllNotSynced()
        if (notSynced.isEmpty()) {
            mSyncState.value = State.Unknown
            return
        }

        mSyncState.value = State.SyncRunning

        val result = firestoreRepo.syncMoodEntries(
            entries = notSynced.map(transform = MoodEntity::toMoodEntry)
        )

        notSynced.forEach {
            localRepo.insertOrUpdateMood(entry = it.copy(isSynchronized = true))
        }


        mSyncState.value = State.SyncFinished(isSuccess = result.isSuccess)
    }
}