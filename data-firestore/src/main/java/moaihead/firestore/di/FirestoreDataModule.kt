@file:Suppress("unused")

package moaihead.firestore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import moaihead.data.DataSourceRepository
import moaihead.data.di.RemoteSource
import moaihead.firestore.FirestoreRepo
import javax.inject.Singleton

/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class FirestoreDataModule {

    @Binds
    @Singleton
    @RemoteSource
    abstract fun bindRemoteDataSource(impl: FirestoreRepo): DataSourceRepository
}