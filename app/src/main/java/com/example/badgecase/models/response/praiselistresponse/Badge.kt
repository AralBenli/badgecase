package com.example.badgecase.models.response.praiselistresponse


import com.google.gson.annotations.SerializedName

data class Badge(
    @SerializedName("lookupId")
    val lookupId: Int,
    @SerializedName("lookupValue")
    val lookupValue: String
)