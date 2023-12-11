package com.example.badgecase.models.response.praiselistresponse


import com.google.gson.annotations.SerializedName

data class PraiseResultItem(
    @SerializedName("ID")
    val iD: String,
    @SerializedName("Author")
    val author: List<Author>,
    @SerializedName("Badge")
    val badge: List<Badge>,
    @SerializedName("RelatedPerson")
    val relatedPerson: List<RelatedPerson>,
    @SerializedName("Message")
    val message: String,
    @SerializedName("Created")
    val created: String,
    @SerializedName("PraiseRating")
    val praiseRating: String,
    @SerializedName("PraiseRating.")
    val praiseRatingWithDot: String,
    @SerializedName("Author.id")
    val authorId: String,
    @SerializedName("Author.title")
    val authorTitle: String,
    @SerializedName("Title")
    val title: String
)