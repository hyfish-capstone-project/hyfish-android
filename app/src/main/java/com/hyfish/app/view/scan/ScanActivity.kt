package com.hyfish.app.view.scan

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.hyfish.app.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity() {
    private var currentImageUri: Uri? = null
    private lateinit var binding: ActivityScanBinding
    private val launchGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                currentImageUri?.let {
                    Log.d(TAG, "Selected image: $it")
//                    binding.previewImage.setImageURI(it)
//                    Gambar preview yang discan
                }
            } else {
                Log.d(IMAGE_PICKER_TAG, "No media selected")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.galleryButton.setOnClickListener {
//            launchGallery.launch(
//                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//            )
//        }

//        binding.scanButton.setOnClickListener {
//            if (currentImageUri != null) {
//                val intent = Intent(this, ScanResultActivity::class.java)
//                intent.putExtra(ScanResultActivity.EXTRA, currentImageUri.toString())
//                startActivity(intent)
//            } else {
//                showToast(getString(R.string.no_image_found))
//            }
//        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "ImageURI"
        private const val IMAGE_PICKER_TAG = "ImagePicker"
    }
}