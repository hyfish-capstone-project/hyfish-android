package com.hyfish.app.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FishResponse(

	@field:SerializedName("data")
	val data: List<FishItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
) : Parcelable

@Parcelize
data class FishItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("recipes")
	val recipes: List<RecipesItem>? = null,

	@field:SerializedName("images")
	val images: List<String>? = null,
) : Parcelable

@Parcelize
data class RecipesItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("ingredients")
	val ingredients: List<IngredientsItem>,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("steps")
	val steps: List<StepsItem>
) : Parcelable

@Parcelize
data class StepsItem(

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("order")
	val order: Int
) : Parcelable

@Parcelize
data class IngredientsItem(

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("measurement")
	val measurement: String
) : Parcelable
