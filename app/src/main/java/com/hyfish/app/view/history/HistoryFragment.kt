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

    private var currentCaptures: List<CaptureItem>? = null

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

        binding.fabCreateScan.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.captures.observe(viewLifecycleOwner) { captures ->
            (binding.rvCaptures.adapter as CaptureAdapter).submitList(captures)
            currentCaptures = captures // Update currentCaptures when data changes
        }
    }

    override fun onResume() {
        super.onResume()
        // Check if the data has changed
        viewModel.captures.value?.let { newCaptures ->
            if (newCaptures != currentCaptures) {
                viewModel.getCaptures()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
