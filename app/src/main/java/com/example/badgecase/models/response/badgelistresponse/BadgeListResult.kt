package com.example.badgecase.models.response.badgelistresponse


import com.google.gson.annotations.SerializedName

data class BadgeListResult(
    @SerializedName("BadgeIcon")
    val badgeIcon: String,
    @SerializedName("ID")
    val iD: Int,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Title")
    val title: String
)