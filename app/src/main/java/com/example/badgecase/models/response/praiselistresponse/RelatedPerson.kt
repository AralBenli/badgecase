package com.example.badgecase.models.response.praiselistresponse


import com.google.gson.annotations.SerializedName

data class RelatedPerson(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String
)