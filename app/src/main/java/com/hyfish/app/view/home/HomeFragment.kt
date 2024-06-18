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
import com.hyfish.app.databinding.FragmentHomeBinding
import com.hyfish.app.view.MainViewModel
import com.hyfish.app.view.ViewModelFactory
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

    private fun getData() {
        viewModel.getArticles()
        historyViewModel.getCapturesWithFishes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            } else {
                binding.tvGreetings.text = getString(R.string.main_greetings, user.username)
                getData()
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
        captureAdapter.setOnItemClickCallback(object : CaptureAdapter.OnItemClickCallback {
            override fun onItemClicked(data: CaptureItemWithFish) {
                val intent = Intent(activity, ScanResultActivity::class.java)
                intent.putExtra(ScanResultActivity.EXTRA_CAPTURE, data)
                startActivity(intent)
            }
        })

        historyViewModel.capturesWithFishes.observe(viewLifecycleOwner) { capturesWithFishes ->
            captureAdapter.submitList(capturesWithFishes)
        }

        binding.tvSeallCaptures.setOnClickListener {
            sharedViewModel.selectTab(2)
        }

        binding.fabCreateScan.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            startActivity(intent)
        }

        binding.btLogout.setOnClickListener {
            viewModel.logout()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.articles.value.isNullOrEmpty() || historyViewModel.capturesWithFishes.value.isNullOrEmpty()) {
            getData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}