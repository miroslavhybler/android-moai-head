@file:Suppress("unused")

package mir.oslav.moaihead.di // Or your app's package

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import moaihead.data.DataSourceRepository
import moaihead.data.di.RemoteSource
import javax.inject.Singleton

/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {


    @Binds
    @Singleton
    abstract fun bindDataSource(
        @RemoteSource dataSource: DataSourceRepository,
    ): DataSourceRepository
}
