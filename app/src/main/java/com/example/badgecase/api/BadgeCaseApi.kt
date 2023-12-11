package com.example.badgecase.api

import com.example.badgecase.models.response.badgelistresponse.BadgeListResponse
import com.example.badgecase.models.response.praiselistresponse.PraiseListResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by AralBenli on 8.12.2023.
 */
interface BadgeCaseApi {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/")
    suspend fun getBadgeList(@Body request: BadgeListRequest): Response<BadgeListResponse>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/")
    suspend fun getPraiseList(@Body request: PraiseListRequest): Response<PraiseListResponse>

}

data class BadgeListRequest(
    val endpoint: String
)

data class PraiseListRequest(
    val endpoint: String
)
