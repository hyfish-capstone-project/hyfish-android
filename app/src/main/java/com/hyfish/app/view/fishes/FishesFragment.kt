package com.hyfish.app.view.fishes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.hyfish.app.data.api.FishItem
import com.hyfish.app.databinding.FragmentFishesBinding
import com.hyfish.app.view.ViewModelFactory

class FishesFragment : Fragment() {
    private val viewModel by viewModels<FishesViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentFishesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFishesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        binding.rvFishes.layoutManager = GridLayoutManager(activity, 2)
        val fishesAdapter = FishesAdapter()
        binding.rvFishes.adapter = fishesAdapter
        fishesAdapter.setOnItemClickCallback(object : FishesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FishItem) {
                val intent = Intent(activity, FishesDetailActivity::class.java)
                intent.putExtra(FishesDetailActivity.EXTRA_FISH, data)
                startActivity(intent)
            }
        })
        viewModel.fishes.observe(viewLifecycleOwner) { captures ->
            fishesAdapter.submitList(captures)
            binding.emptyText.visibility = if (captures.isNullOrEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.getFishes()

        binding.emptyText.visibility =
            if (viewModel.fishes.value.isNullOrEmpty()) View.VISIBLE else View.GONE


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}