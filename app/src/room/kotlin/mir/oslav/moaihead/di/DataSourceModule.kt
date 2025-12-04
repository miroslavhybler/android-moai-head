package mir.oslav.moaihead.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import moaihead.data.DataSourceRepository
import moaihead.data.di.LocalSource
import javax.inject.Singleton


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    // When the app asks for a 'DataSource', provide the one
    // that is labeled with @LocalSource.
    @Binds
    @Singleton
    abstract fun bindDataSource(@LocalSource dataSourceRepository: DataSourceRepository): DataSourceRepository
}