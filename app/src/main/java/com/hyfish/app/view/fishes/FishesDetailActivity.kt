package com.hyfish.app.view.fishes

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.hyfish.app.data.api.FishItem
import com.hyfish.app.databinding.ActivityFishesDetailBinding
import com.hyfish.app.view.ViewModelFactory

class FishesDetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<FishDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

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

            viewModel.fishDetail.observe(this) { fishDetail ->
                if (fishDetail != null) {
                    binding.tvFishName.text = fishDetail.name
                    binding.tvDescription.text = fishDetail.description

                    if (fishDetail.nutritionImage.isNotEmpty()) {
                        binding.ivNutrition.visibility = View.VISIBLE
                        Glide.with(binding.root.context).load(fishDetail.nutritionImage)
                            .into(binding.ivNutrition)
                    } else {
                        binding.ivNutrition.visibility = View.GONE
                    }

                    var recipesText = ""
                    fishDetail.recipes.forEach { recipe ->
                        recipesText += recipe.name + "\n\n"
                        recipesText += "Ingredients:\n"
                        recipe.ingredients?.forEach { ingredient ->
                            recipesText += " - ${ingredient?.name} (${ingredient?.amount} ${ingredient?.measurement})\n"
                        }

                        recipesText += "\n"
                        recipesText += "Steps:\n"
                        recipe.steps?.forEach { step ->
                            recipesText += " - ${step?.description}\n"
                        }
                        recipesText += "\n\n\n"
                    }
                    binding.tvRecipesText.text = recipesText.ifEmpty { "No recipes available" }
                }
            }

            viewModel.loading.observe(this) {
                binding.progressIndicator.visibility = if (it) View.VISIBLE else View.GONE
            }

            viewModel.getFish(fish.id)
        }
    }

    companion object {
        const val EXTRA_FISH = "extra_fish"
    }
}