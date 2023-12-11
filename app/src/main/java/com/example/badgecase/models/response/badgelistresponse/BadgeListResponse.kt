package com.example.badgecase.models.response.badgelistresponse


import com.google.gson.annotations.SerializedName

data class BadgeListResponse(
    @SerializedName("value")
    val badgeList: List<BadgeListResult>
)