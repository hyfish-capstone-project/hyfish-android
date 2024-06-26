package com.hyfish.app.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyfish.app.R
import com.hyfish.app.data.api.CaptureItemWithFish
import com.hyfish.app.data.api.PostItem
import com.hyfish.app.databinding.FragmentHomeBinding
import com.hyfish.app.view.MainViewModel
import com.hyfish.app.view.ViewModelFactory
import com.hyfish.app.view.forum.post.PostDetailActivity
import com.hyfish.app.view.history.HistoryViewModel
import com.hyfish.app.view.login.LoginActivity
import com.hyfish.app.view.scan.ScanActivity
import com.hyfish.app.view.scan.ScanResultActivity

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val sharedViewModel by activityViewModels<MainViewModel>()

    private val historyViewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvArticles.layoutManager = LinearLayoutManager(activity).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        binding.rvCaptures.layoutManager = LinearLayoutManager(activity)

        binding.tvSeallCaptures.setOnClickListener {
            sharedViewModel.selectTab(2)
        }

        setupSession()

        showLoading()

        setupArticleAdapter()

        setupCaptureAdapter()

        setupScanButton()

        setupLogoutButton()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.articles.value.isNullOrEmpty()) {
            viewModel.getArticles()
        }
        if (historyViewModel.capturesWithFishes.value.isNullOrEmpty()) {
            historyViewModel.getCapturesWithFishes()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            } else {
                binding.tvGreetings.text = getString(R.string.main_greetings, user.role)
                viewModel.getArticles()
                historyViewModel.getCapturesWithFishes()
            }
        }
    }

    private fun showLoading() {
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressIndicatorA.visibility = if (it) View.VISIBLE else View.GONE
        }

        historyViewModel.loading.observe(viewLifecycleOwner) {
            binding.progressIndicatorRC.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setupArticleAdapter() {
        val articleAdapter = ArticleAdapter()
        binding.rvArticles.adapter = articleAdapter

        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            articleAdapter.submitList(articles)
            binding.tvEmptyA.visibility = if (articles.isNullOrEmpty()) View.VISIBLE else View.GONE
        }

        articleAdapter.setOnItemClickCallback(object : ArticleAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PostItem) {
                val intent = Intent(activity, PostDetailActivity::class.java)
                intent.putExtra(PostDetailActivity.EXTRA_POST, data)
                intent.putExtra(PostDetailActivity.EXTRA_IS_ARTICLE, true)
                startActivity(intent)
            }
        })
    }

    private fun setupScanButton() {
        binding.fabCreateScan.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLogoutButton() {
        binding.btLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun setupCaptureAdapter() {
        val captureAdapter = CaptureAdapter()
        binding.rvCaptures.adapter = captureAdapter
        captureAdapter.setOnItemClickCallback(object : CaptureAdapter.OnItemClickCallback {
            override fun onItemClicked(data: CaptureItemWithFish) {
                val intent = Intent(activity, ScanResultActivity::class.java)
                intent.putExtra(ScanResultActivity.EXTRA_CAPTURE, data)
                startActivity(intent)
            }
        })

        historyViewModel.capturesWithFishes.observe(viewLifecycleOwner) { capturesWithFishes ->
            captureAdapter.submitList(capturesWithFishes.take(3))
            binding.tvEmptyRc.visibility =
                if (capturesWithFishes.isNullOrEmpty()) View.VISIBLE else View.GONE
        }
    }
}