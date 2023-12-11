package com.example.badgecase.models.response.praiselistresponse


import com.google.gson.annotations.SerializedName

data class PraiseListResponse(
    @SerializedName("Row")
    val praiseResultItem: List<PraiseResultItem>,
)