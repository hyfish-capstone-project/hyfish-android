package com.hyfish.app.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForumResponse(

	@field:SerializedName("data")
	val data: List<PostItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
) : Parcelable

@Parcelize
data class PostItem(

	@field:SerializedName("images")
	val images: List<String>,

	@field:SerializedName("like")
	val like: Int,

	@field:SerializedName("comment")
	val comment: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("body")
	val body: String
) : Parcelable
