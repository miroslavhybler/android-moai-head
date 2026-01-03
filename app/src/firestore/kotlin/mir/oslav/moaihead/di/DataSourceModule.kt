@file:Suppress("unused")

package mir.oslav.moaihead.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import moaihead.data.BaseDataSourceRepository
import moaihead.data.LocalToRemoteSyncer
import moaihead.data.di.RemoteSource
import moaihead.data.model.SyncState
import moaihead.firestore.LocalToRemoteSyncRepo
import javax.inject.Inject

/**
 * @author Miroslav Hýbler <br>
 * created on 09.12.2025
 */
class FirestoreLocalToRemoteSyncer @Inject constructor(
    private val firestoreRepo: LocalToRemoteSyncRepo
) : LocalToRemoteSyncer {
    override val syncState: Flow<SyncState>
        get() = firestoreRepo.syncState.map {
            when (it) {
                is LocalToRemoteSyncRepo.State.SyncFinished -> SyncState.SyncFinished(isSuccess = it.isSuccess)
                LocalToRemoteSyncRepo.State.SyncRunning -> SyncState.SyncRunning
                LocalToRemoteSyncRepo.State.Unknown -> SyncState.Unknown
            }
        }

    override fun requestSync() {
        CoroutineScope(context = Dispatchers.IO).launch {
            firestoreRepo.tryRequestSync()
        }
    }
}


@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindSyncer(
        impl: FirestoreLocalToRemoteSyncer,
    ): LocalToRemoteSyncer

    companion object {
        @Provides
        fun provideDataSourceRepository(
            @RemoteSource repo: BaseDataSourceRepository,
        ): BaseDataSourceRepository {
            return repo
        }
    }
}