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

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("comments")
    var comments: List<CommentsItem>,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("body")
	val body: String,

	@field:SerializedName("likes")
	val likes: Int,

	@field:SerializedName("tags")
	val tags: List<String>
) : Parcelable

@Parcelize
data class CommentsItem(

	@field:SerializedName("replies")
	val replies: List<CommentsItem>,

	@field:SerializedName("author")
	val author: Author,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class RepliesItem(

	@field:SerializedName("author")
	val author: Author,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class Author(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("username")
	val username: String
) : Parcelable
