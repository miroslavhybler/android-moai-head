package mir.oslav.moaihead.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import moaihead.data.DataSourceRepository
import moaihead.data.LocalToRemoteSyncer
import moaihead.data.di.LocalSource
import moaihead.data.model.SyncState
import javax.inject.Inject

class NoOpLocalToRemoteSyncer @Inject constructor() : LocalToRemoteSyncer {
    override val syncState: Flow<SyncState> = flowOf(SyncState.Unknown)
    override fun requestSync() = Unit
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindSyncer(impl: NoOpLocalToRemoteSyncer): LocalToRemoteSyncer

    companion object {
        @Provides
        fun provideDataSourceRepository(@LocalSource localRepository: DataSourceRepository): DataSourceRepository = localRepository
    }
}