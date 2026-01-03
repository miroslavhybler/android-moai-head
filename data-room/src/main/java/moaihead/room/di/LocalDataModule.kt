@file:Suppress("unused")

package moaihead.room.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import moaihead.data.BaseDataSourceRepository
import moaihead.data.di.LocalSource
import moaihead.room.LocalDatabaseRepo
import javax.inject.Singleton

/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataModule {

    @Binds
    @Singleton
    @LocalSource
    abstract fun bindLocalDataSource(
        impl: LocalDatabaseRepo,
    ): BaseDataSourceRepository
}