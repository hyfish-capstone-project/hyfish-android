package com.hyfish.app.view.forum.post

import android.Manifest
import android.app.Activity
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hyfish.app.R
import com.hyfish.app.databinding.ActivityPostAddBinding
import com.hyfish.app.util.getImageUri
import com.hyfish.app.util.reduceFileImage
import com.hyfish.app.util.uriToFile
import com.hyfish.app.view.ViewModelFactory

class PostAddActivity : AppCompatActivity() {
    private val viewModel by viewModels<PostAddViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val binding get() = _binding!!

    private var _binding: ActivityPostAddBinding? = null

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityPostAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupAction()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun uploadImage() {
        val title = binding.inTitle.text.toString()
        val body = binding.inBody.text.toString()
        val tags = emptyList<String>()
        val images = if (currentImageUri != null) {
            val imageFile = uriToFile(currentImageUri!!, this).reduceFileImage()
            listOf(imageFile)
        } else {
            emptyList()
        }

        viewModel.createPost(title, body, tags, images)
    }

    private fun showImage() {
        if (currentImageUri == null) {
            binding.ivImage.visibility = View.GONE
            return
        }

        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivImage.setImageURI(it)
            binding.ivImage.visibility = View.VISIBLE
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
                showImage()
            }
        }
    }

    private fun setupAction() {
        viewModel.status.observe(this) { success ->
            if (success) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        viewModel.loading.observe(this) {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(this) {
            it.getContentIfNotHandled()?.let { message ->
                showError(message)
            }
        }

        binding.btGallery.setOnClickListener {
            startGallery()
        }

        binding.btCamera.setOnClickListener {
            startCamera()
        }

        binding.btSubmit.setOnClickListener {
            uploadImage()
        }
    }

    private fun showError(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.error))
            setMessage(message)
            setPositiveButton(getString(R.string.ok)) { _, _ -> }
            create()
            show()
        }
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
