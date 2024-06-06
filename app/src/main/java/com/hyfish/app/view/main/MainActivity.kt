package com.hyfish.app.view.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyfish.app.R
import com.hyfish.app.data.api.ArticleItem
import com.hyfish.app.data.api.CaptureItem
import com.hyfish.app.databinding.ActivityMainBinding
import com.hyfish.app.view.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
//                startActivity(Intent(this, LoginActivity::class.java))
//                finish()
            } else {
                binding.tvGreetings.text = getString(R.string.main_greetings, user.username)
            }
        }

        binding.rvArticles.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        val articleAdapter = ArticleAdapter()
        binding.rvArticles.adapter = articleAdapter

        val dummyArticles = mutableListOf<ArticleItem>()
        for (i in 0..10) {
            dummyArticles.add(
                ArticleItem(
                    title = "Article $i",
                    body = "This is the body of article $i",
                    images = listOf("https://picsum.photos/200/300"),
                    like = 99,
                    comment = 99
                )
            )
        }
        articleAdapter.submitList(dummyArticles)

        binding.rvCaptures.layoutManager = LinearLayoutManager(this)
        val captureAdapter = CaptureAdapter()
        binding.rvCaptures.adapter = captureAdapter

        val dummyCaptures = mutableListOf<CaptureItem>()
        for (i in 0..10) {
            dummyCaptures.add(
                CaptureItem(
                    image = "https://picsum.photos/200/300",
                    result = "Result $i",
                    createdAt = "2021-08-01 12:00:00",
                    id = i,
                    rate = 99
                )
            )
        }
        captureAdapter.submitList(dummyCaptures)
    }
}
