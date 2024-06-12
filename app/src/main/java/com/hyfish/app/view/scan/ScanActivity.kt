package com.hyfish.app.view.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hyfish.app.R
import com.hyfish.app.databinding.ActivityScanBinding
import com.hyfish.app.util.getImageUri

class ScanActivity : AppCompatActivity() {
    private var currentImageUri: Uri? = null

    private lateinit var binding: ActivityScanBinding

    private val launchGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                currentImageUri?.let {
                    Log.d(TAG, "Selected image: $it")
                    binding.previewImageView.setImageURI(it)
                }
            } else {
                Log.d(IMAGE_PICKER_TAG, "No media selected")
            }
        }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(
                this, "Permission request granted", Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                this, "Permission request denied", Toast.LENGTH_LONG
            ).show()
        }
    }

    private val launchCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
            if (isSuccessful) {
                currentImageUri?.let {
                    Log.d(TAG, "Selected image: $it")
                    binding.previewImageView.setImageURI(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener {
            launchGallery.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.scanButton.setOnClickListener {
            if (currentImageUri != null) {
                val intent = Intent(this, ScanResultActivity::class.java)
                intent.putExtra(ScanResultActivity.EXTRA, currentImageUri.toString())
                startActivity(intent)
            } else {
                showToast(getString(R.string.no_image_found))
            }
        }

        binding.cameraButton.setOnClickListener {
            if (!permissionGranted()) {
                requestPermission.launch(PERMISSION)
            } else {
                currentImageUri = getImageUri(this)
                launchCamera.launch(currentImageUri!!)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun permissionGranted() = ContextCompat.checkSelfPermission(
        this, PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val TAG = "ImageURI"
        private const val IMAGE_PICKER_TAG = "ImagePicker"
        private const val PERMISSION = Manifest.permission.CAMERA
    }
}
