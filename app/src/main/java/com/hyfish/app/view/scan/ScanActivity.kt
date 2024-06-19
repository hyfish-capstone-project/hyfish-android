package com.hyfish.app.view.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hyfish.app.R
import com.hyfish.app.data.api.CaptureItemWithFish
import com.hyfish.app.data.api.FishItem
import com.hyfish.app.databinding.ActivityScanBinding
import com.hyfish.app.util.getImageUri
import com.hyfish.app.util.reduceFileImage
import com.hyfish.app.util.uriToFile
import com.hyfish.app.view.ViewModelFactory

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding

    private val viewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null

    private var cachedFishes = emptyList<FishItem>()

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
            startCamera()
        } else {
            showToast(getString(R.string.no_permission))
        }
    }

    private val launchCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccessful ->
        if (isSuccessful) {
            currentImageUri?.let {
                Log.d(TAG, "Selected image: $it")
                binding.previewImageView.setImageURI(it)
            }
        }
    }

    enum class ScanType(val value: String) {
        FRESHNESS("freshness"), CLASSIFICATION("classification"),
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.title_scan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showLoading()

        showError()

        setupCaptureItem()

        setupOnClickButton()

        viewModel.fishes.observe(this) { fishes ->
            cachedFishes = fishes
        }
        viewModel.getFishes()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun startCamera() {
        if (!permissionGranted()) {
            requestPermission.launch(Manifest.permission.CAMERA)
        } else {
            currentImageUri = getImageUri(this)
            launchCamera.launch(currentImageUri!!)
        }
    }

    private fun permissionGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun uploadImage(type: ScanType) {
        if (currentImageUri != null) {
            currentImageUri?.let { uri ->
                val typeStr = type.value
                val imageFile = uriToFile(uri, this).reduceFileImage()

                viewModel.uploadScan(typeStr, imageFile)
            }
        } else {
            showToast(getString(R.string.no_image_found))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading() {
        viewModel.loading.observe(this) {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun showError() {
        viewModel.error.observe(this) {
            it.getContentIfNotHandled()?.let { message ->
                showToast(message)
            }
        }
    }

    private fun setupCaptureItem() {
        viewModel.captureItem.observe(this) { captureItem ->
            Log.d("captureItem", "Capture item: $captureItem")
            val captureItemWithFish = CaptureItemWithFish(score = captureItem.score,
                updatedAt = captureItem.updatedAt,
                userId = captureItem.userId,
                imageUrl = captureItem.imageUrl,
                createdAt = captureItem.createdAt,
                id = captureItem.id,
                type = captureItem.type,
                freshness = captureItem.freshness,
                fishId = captureItem.fishId,
                fish = cachedFishes.firstOrNull { it.id == captureItem.fishId })
            val intent = Intent(this, ScanResultActivity::class.java)
            intent.putExtra(ScanResultActivity.EXTRA_CAPTURE, captureItemWithFish)
            startActivity(intent)
            finish()
        }
    }

    private fun setupOnClickButton() {
        binding.galleryButton.setOnClickListener {
            launchGallery.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.cameraButton.setOnClickListener {
            startCamera()
        }

        binding.freshnessButton.setOnClickListener {
            uploadImage(ScanType.FRESHNESS)
        }

        binding.classificationButton.setOnClickListener {
            uploadImage(ScanType.CLASSIFICATION)
        }
    }

    companion object {
        private const val TAG = "ImageURI"
        private const val IMAGE_PICKER_TAG = "ImagePicker"
    }
}
