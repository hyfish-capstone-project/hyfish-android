package com.hyfish.app.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreatePostResponse(

	@field:SerializedName("data")
	val data: CreatePostItem,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
) : Parcelable

@Parcelize
data class CreatePostItem(

	@field:SerializedName("images")
	val images: List<ImagesItem?>,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("body")
	val body: String,

	@field:SerializedName("tags")
	val tags: List<TagsItem>
) : Parcelable

@Parcelize
data class TagsItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int? = null,
) : Parcelable

@Parcelize
data class ImagesItem(

	@field:SerializedName("post_id")
	val postId: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int
) : Parcelable
