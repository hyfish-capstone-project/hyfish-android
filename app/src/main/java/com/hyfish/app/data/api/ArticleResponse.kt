package com.hyfish.app.data.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleResponse(
    val data: List<PostItem>, val message: String, val status: Boolean
) : Parcelable

@Parcelize
data class ArticleItem(
    val images: List<String>, val like: Int, val comment: Int, val title: String, val body: String
) : Parcelable
