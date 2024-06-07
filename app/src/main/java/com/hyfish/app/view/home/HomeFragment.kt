package com.hyfish.app.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyfish.app.R
import com.hyfish.app.data.api.ArticleItem
import com.hyfish.app.data.api.CaptureItem
import com.hyfish.app.databinding.FragmentHomeBinding
import com.hyfish.app.view.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelFactory.getInstance(requireActivity()).create(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
//                startActivity(Intent(this, LoginActivity::class.java))
//                finish()
            } else {
                binding.tvGreetings.text = getString(R.string.main_greetings, user.username)
            }
        }

        binding.rvArticles.layoutManager = LinearLayoutManager(activity).apply {
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

        binding.rvCaptures.layoutManager = LinearLayoutManager(activity)
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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}