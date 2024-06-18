package com.hyfish.app.view.fishes

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.hyfish.app.data.api.FishItem
import com.hyfish.app.databinding.ActivityFishesDetailBinding

class FishesDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFishesDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFishesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getFishDetail()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    @Suppress("DEPRECATION")
    private fun getFishDetail() {
        val fish = intent.getParcelableExtra<FishItem>(EXTRA_FISH)

        if (fish != null) {
            if (fish.images.isNotEmpty()) {
                binding.ivImage.visibility = View.VISIBLE
                Glide.with(binding.root.context).load(fish.images[0]).into(binding.ivImage)
            } else {
                binding.ivImage.visibility = View.GONE
            }

            binding.tvFishName.text = fish.name
            binding.tvDescription.text = fish.description

            if (fish.nutritionImageUrl?.isNotEmpty() == true) {
                binding.ivNutrition.visibility = View.VISIBLE
                Glide.with(binding.root.context).load(fish.nutritionImageUrl)
                    .into(binding.ivNutrition)
            } else {
                binding.ivNutrition.visibility = View.GONE
            }
        }
    }

    companion object {
        const val EXTRA_FISH = "extra_fish"
    }
}