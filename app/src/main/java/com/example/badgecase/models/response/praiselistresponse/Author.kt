package com.example.badgecase.models.response.praiselistresponse


import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String
)