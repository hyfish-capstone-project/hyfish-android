package com.hyfish.app.view.forum

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyfish.app.data.api.PostItem
import com.hyfish.app.databinding.FragmentForumBinding
import com.hyfish.app.view.forum.post.PostAddActivity

class ForumFragment : Fragment() {

    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val viewModel = ViewModelFactory.getInstance(requireActivity()).create(ForumViewModel::class.java)

        _binding = FragmentForumBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvPosts.layoutManager = LinearLayoutManager(activity)
        val postAdapter = PostAdapter()
        binding.rvPosts.adapter = postAdapter

        val dummyPost = mutableListOf<PostItem>()
        for (i in 0..10) {
            dummyPost.add(
                PostItem(
                    title = "Post $i",
                    body = "This is the body of post $i",
                    images = if (i % 2 == 0) listOf("https://picsum.photos/200/300") else emptyList(),
                    like = 99,
                    comment = 99
                )
            )
        }
        postAdapter.submitList(dummyPost)

        binding.fabCreatePost.setOnClickListener {
            val intent = Intent(activity, PostAddActivity::class.java)
            startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}