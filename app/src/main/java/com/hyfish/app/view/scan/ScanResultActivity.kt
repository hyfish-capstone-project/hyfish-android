package com.hyfish.app.view.scan

import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.hyfish.app.R
import com.hyfish.app.data.api.CaptureItemWithFish
import com.hyfish.app.databinding.ActivityScanResultBinding
import com.hyfish.app.util.toReadableDate
import com.hyfish.app.view.fishes.FishesDetailActivity

class ScanResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupCaptureItem()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setupCaptureItem() {
        val item = intent.getParcelableExtra<CaptureItemWithFish>(EXTRA_CAPTURE)

        Log.d("CaptureItem", "Showing item $item")
        if (item != null) {
            Glide.with(binding.root.context).load(item.imageUrl).into(binding.ivImage)

            binding.tvClassification.visibility = View.GONE
            binding.cvClassification.visibility = View.GONE
            binding.tvFreshness.visibility = View.GONE
            binding.cvFresh.visibility = View.GONE
            binding.cvNotFresh.visibility = View.GONE

            val scoreStr: String = DecimalFormat("0.00").format(item.score.times(100))
            binding.tvScore.text = getString(R.string.score, scoreStr)
            binding.tvCreatedAt.text = getString(R.string.created_at, item.createdAt.toReadableDate())

            when (item.type) {
                ScanActivity.ScanType.CLASSIFICATION.value -> {
                    item.fish?.let { fish ->
                        if (fish.images.isNotEmpty()) {
                            Glide.with(binding.root.context).load(fish.images[0])
                                .into(binding.ivFishPhoto)
                        }

                        binding.tvFishName.text = fish.name
                        binding.tvClassification.visibility = View.VISIBLE
                        binding.cvClassification.visibility = View.VISIBLE

                        binding.cvClassification.setOnClickListener {
                            val intent = Intent(this, FishesDetailActivity::class.java)
                            intent.putExtra(FishesDetailActivity.EXTRA_FISH, fish)
                            startActivity(intent)
                        }
                    }
                }

                ScanActivity.ScanType.FRESHNESS.value -> {
                    if (item.freshness?.lowercase()?.contains("not") == true) {
                        binding.tvNotFresh.text = item.freshness
                        binding.cvNotFresh.visibility = View.VISIBLE
                    } else {
                        binding.tvFresh.text = item.freshness
                        binding.cvFresh.visibility = View.VISIBLE
                    }
                    binding.tvFreshness.visibility = View.VISIBLE
                }

                else -> {
                    throw IllegalArgumentException("Unknown scan type")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val EXTRA_CAPTURE = "extra_capture"
    }
}