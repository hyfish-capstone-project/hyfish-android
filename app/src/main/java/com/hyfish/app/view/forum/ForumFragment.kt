package com.hyfish.app.view.forum

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyfish.app.data.api.PostItem
import com.hyfish.app.databinding.FragmentForumBinding
import com.hyfish.app.view.MainViewModel
import com.hyfish.app.view.ViewModelFactory
import com.hyfish.app.view.forum.post.PostAddActivity
import com.hyfish.app.view.forum.post.PostDetailActivity
import com.hyfish.app.view.forum.post.PostDetailViewModel

class ForumFragment : Fragment() {

    private var _binding: FragmentForumBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<ForumViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val postViewModel by viewModels<PostDetailViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val sharedViewModel by activityViewModels<MainViewModel>()

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

        binding.emptyText.visibility =
            if (viewModel.forums.value.isNullOrEmpty()) View.VISIBLE else View.GONE

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    viewModel.getForums(searchBar.text.toString())
                    false
                }
        }

        viewModel.message.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                showToast(message)
            }
        }

        setupPostAdapter()

        showLoading()

        setupAddPostButton()

        return root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getForums()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupPostAdapter() {
        val postAdapter = PostAdapter()

        binding.rvPosts.adapter = postAdapter

        postAdapter.setOnItemClickCallback(object : PostAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PostItem) {
                val intent = Intent(activity, PostDetailActivity::class.java)
                intent.putExtra(PostDetailActivity.EXTRA_POST, data)
                startActivity(intent)
            }
        })

        postViewModel.newLike.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                viewModel.getForums()
            }
        }

        postAdapter.setOnLikeClickCallback(object : PostAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PostItem) {
                if (data.isLiked) {
                    postViewModel.unlikePost(data.id)
                } else {
                    postViewModel.likePost(data.id)
                }
            }
        })

        viewModel.forums.observe(viewLifecycleOwner) {
            postAdapter.submitList(it)
            binding.emptyText.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showLoading() {
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setupAddPostButton() {
        binding.fabCreatePost.setOnClickListener {
            val intent = Intent(activity, PostAddActivity::class.java)
            addPostLauncher.launch(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }
}
