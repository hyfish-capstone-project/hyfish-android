package com.hyfish.app.view.forum

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyfish.app.data.api.PostItem
import com.hyfish.app.databinding.FragmentForumBinding
import com.hyfish.app.view.ViewModelFactory
import com.hyfish.app.view.forum.post.PostAddActivity
import com.hyfish.app.view.forum.post.PostDetailActivity

class ForumFragment : Fragment() {

    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ForumViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val addPostLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.getForums() // Refresh data
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForumBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvPosts.layoutManager = LinearLayoutManager(activity)
        val postAdapter = PostAdapter()
        binding.rvPosts.adapter = postAdapter
        postAdapter.setOnItemClickCallback(object : PostAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PostItem) {
                val intent = Intent(activity, PostDetailActivity::class.java)
                intent.putExtra(PostDetailActivity.EXTRA_POST, data)
                startActivity(intent)
            }
        })

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.forums.observe(viewLifecycleOwner) {
            postAdapter.submitList(it)
        }

        binding.fabCreatePost.setOnClickListener {
            val intent = Intent(activity, PostAddActivity::class.java)
            addPostLauncher.launch(intent)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.forums.value.isNullOrEmpty()) {
            viewModel.getForums()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
