package com.example.badgecase.repository

import com.example.badgecase.api.BadgeListRequest
import com.example.badgecase.api.PraiseListRequest
import com.example.badgecase.di.ApiResponse
import com.example.badgecase.models.response.badgelistresponse.BadgeListResponse
import com.example.badgecase.models.response.praiselistresponse.PraiseListResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.http.Body

/**
 * Created by AralBenli on 8.12.2023.
 */
interface IBadgeCaseRepository {

    fun getBadgeList(
        @Body request: BadgeListRequest
    ): Flow<ApiResponse<BadgeListResponse?>>

    fun getPraiseList(
        @Body request: PraiseListRequest
    ): Flow<ApiResponse<PraiseListResponse?>>

}