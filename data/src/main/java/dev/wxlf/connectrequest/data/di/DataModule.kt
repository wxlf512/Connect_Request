package dev.wxlf.connectrequest.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.wxlf.connectrequest.data.remote.StatAPI
import dev.wxlf.connectrequest.data.remote.datasources.RemoteStatDataSource
import dev.wxlf.connectrequest.data.remote.datasources.RetrofitStatDataSource
import dev.wxlf.connectrequest.data.remote.repositories.StatRepository
import dev.wxlf.connectrequest.data.remote.repositories.StatRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesRetrofitStatDataSource(statAPI: StatAPI): RemoteStatDataSource =
        RetrofitStatDataSource(statAPI = statAPI, ioDispatcher = Dispatchers.IO)

    @Provides
    @Singleton
    fun providesStatRepository(remote: RemoteStatDataSource): StatRepository =
        StatRepositoryImpl(
            remote = remote,
            externalScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        )
}