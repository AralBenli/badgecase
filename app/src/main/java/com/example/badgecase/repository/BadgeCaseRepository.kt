package com.example.badgecase.repository

import com.example.badgecase.api.BadgeCaseApi
import com.example.badgecase.api.BadgeListRequest
import com.example.badgecase.api.PraiseListRequest
import com.example.badgecase.di.ApiResponse
import com.example.badgecase.models.response.badgelistresponse.BadgeListResponse
import com.example.badgecase.models.response.praiselistresponse.PraiseListResponse
import com.example.badgecase.utils.result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers
import okhttp3.RequestBody


/**
 * Created by AralBenli on 8.12.2023.
 */
class BadgeCaseRepository(
    private val api: BadgeCaseApi
) : IBadgeCaseRepository {


    override fun getBadgeList(request: BadgeListRequest): Flow<ApiResponse<BadgeListResponse?>> =
        result {
            api.getBadgeList(request)
        }.flowOn(Dispatchers.IO)

    override fun getPraiseList(request: PraiseListRequest): Flow<ApiResponse<PraiseListResponse?>> =
        result {
            api.getPraiseList(request)
        }.flowOn(Dispatchers.IO)

}
