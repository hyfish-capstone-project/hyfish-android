package com.hyfish.app.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.hyfish.app.R
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    var threshold: Float = 0.1f,
    var maxResults: Int = 3,
    val modelName: String = "NAMA ML.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    interface ClassifierListener {
        fun onResults(results: List<Classifications>?, inferenceTime: Long)
        fun onError(error: String)
    }

    init {
        setupImageClassifier()
    }

    @Suppress("DEPRECATION")
    fun classifyStaticImage(imageUri: Uri) {
        try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(
                    context.contentResolver, imageUri
                )
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            }?.copy(Bitmap.Config.ARGB_8888, true)
            bitmap?.let {
                val tensorImage = TensorImage.fromBitmap(bitmap)
                val results = imageClassifier?.classify(tensorImage)
                classifierListener?.onResults(results, 0)
            } ?: classifierListener?.onError(context.getString(R.string.no_error))
        } catch (e: Exception) {
            classifierListener?.onError(context.getString(R.string.cant_classify_image))
            Log.d(TAG, e.message.toString())
        }
    }

    private fun setupImageClassifier() {
        val optionsBuilder =
            ImageClassifier.ImageClassifierOptions.builder().setScoreThreshold(threshold)
                .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder().setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier =
                ImageClassifier.createFromFileAndOptions(context, modelName, optionsBuilder.build())
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.cant_classify_image))
            Log.d(TAG, e.message.toString())
        }
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}