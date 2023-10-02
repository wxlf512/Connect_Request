package dev.wxlf.connectrequest.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.wxlf.connectrequest.data.remote.StatAPI
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesStatAPI(): StatAPI =
        Retrofit.Builder()
            .baseUrl("https://stat-api.airnet.ru/v2/utils/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(StatAPI::class.java)
}