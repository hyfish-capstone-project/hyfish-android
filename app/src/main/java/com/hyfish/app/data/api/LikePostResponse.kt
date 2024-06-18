package com.hyfish.app.data.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LikePostResponse(
	val message: String,
	val status: Boolean
) : Parcelable

@Parcelize
data class UnlikePostResponse(
	val message: String,
	val status: Boolean
) : Parcelable