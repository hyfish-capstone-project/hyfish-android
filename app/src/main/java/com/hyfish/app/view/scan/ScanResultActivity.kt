package com.hyfish.app.view.scan

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.hyfish.app.databinding.ActivityScanResultBinding
import com.hyfish.app.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications

class ScanResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        extractImageUri()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun extractImageUri() {
        val imageUri = intent.getStringExtra(EXTRA)
        if (imageUri != null) {
            val imageUriParser = Uri.parse(imageUri)
            Log.d(TAG, "Showing image $imageUriParser")
            binding.ivImage.setImageURI(imageUriParser)

            val imageClassifierHelper = ImageClassifierHelper(context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                        results?.let {
                            val onTopResult = results[0]
                            val label = onTopResult.categories[0].label
                            val score = onTopResult.categories[0].score

                            fun Float.turnToString(): String {
                                return String.format("%.2f%%", this * 100)
                            }
//                            binding.resultText.text = "$label ${score.turnToString()}"
//                            Text hasil scan
                        }
                    }

                    override fun onError(error: String) {
                        Log.d(TAG, "Error: $error")
                        showError(error)
                    }
                })
            imageClassifierHelper.classifyStaticImage(imageUriParser)
        } else {
            Log.d(TAG, "No image found")
            finish()
        }
    }

    private fun showError(error: String) {
//        binding.resultText.text = error
//        Text hasil scan error
    }

    companion object {
        const val EXTRA = "extra_uri"
        private const val TAG = "ImagePicker"
    }
}