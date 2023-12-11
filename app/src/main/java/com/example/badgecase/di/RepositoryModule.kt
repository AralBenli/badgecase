package com.example.badgecase.di

import com.example.badgecase.api.BadgeCaseApi
import com.example.badgecase.repository.BadgeCaseRepository
import com.example.badgecase.repository.IBadgeCaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by AralBenli on 8.12.2023.
 */
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideBadgeCaseRepository(badgeCaseApi: BadgeCaseApi): IBadgeCaseRepository {
        return BadgeCaseRepository(badgeCaseApi)
    }
}