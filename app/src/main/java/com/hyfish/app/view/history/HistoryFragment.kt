package com.hyfish.app.view.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyfish.app.data.api.CaptureItem
import com.hyfish.app.databinding.FragmentHistoryBinding
import com.hyfish.app.view.ViewModelFactory
import com.hyfish.app.view.home.CaptureAdapter
import com.hyfish.app.view.scan.ScanActivity
import com.hyfish.app.view.scan.ScanResultActivity

class HistoryFragment : Fragment() {
    private val viewModel by activityViewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.GONE
        }

        binding.rvCaptures.layoutManager = LinearLayoutManager(activity)
        val captureAdapter = CaptureAdapter()
        binding.rvCaptures.adapter = captureAdapter
        captureAdapter.setOnItemClickCallback(object : CaptureAdapter.OnItemClickCallback {
            override fun onItemClicked(data: CaptureItem) {
                val intent = Intent(activity, ScanResultActivity::class.java)
                intent.putExtra(ScanResultActivity.EXTRA_CAPTURE, data)
                startActivity(intent)
            }
        })

        viewModel.captures.observe(viewLifecycleOwner) { captures ->
            captureAdapter.submitList(captures)
        }

        viewModel.getCaptures()

        binding.fabCreateScan.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    //    TODO: cari pengganti onResume buat refresh data, soalnya kalo gini tiap kali balik ke fragment ini data di load ulang
    override fun onResume() {
        super.onResume()
        viewModel.getCaptures()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}