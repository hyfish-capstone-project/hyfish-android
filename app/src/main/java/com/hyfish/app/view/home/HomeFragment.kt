package com.hyfish.app.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyfish.app.R
import com.hyfish.app.data.api.CaptureItem
import com.hyfish.app.databinding.FragmentHomeBinding
import com.hyfish.app.view.ViewModelFactory
import com.hyfish.app.view.login.LoginActivity
import com.hyfish.app.view.scan.ScanActivity

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

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            } else {
                binding.tvGreetings.text = getString(R.string.main_greetings, user.role)
                viewModel.getArticles()
            }
        }

        binding.rvArticles.layoutManager = LinearLayoutManager(activity).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        val articleAdapter = ArticleAdapter()
        binding.rvArticles.adapter = articleAdapter

        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            articleAdapter.submitList(articles)
        }

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

        binding.fabCreateScan.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            startActivity(intent)
        }

        binding.btLogout.setOnClickListener {
            viewModel.logout()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}