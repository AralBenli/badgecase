package com.example.badgecase.di

import com.example.badgecase.api.BadgeCaseApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

/**
 * Created by AralBenli on 8.12.2023.
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideBadgeCaseApi(@Named("provideRetrofit") retrofit: Retrofit): BadgeCaseApi {
        return retrofit.create(BadgeCaseApi::class.java)
    }
}