package com.hyfish.app.view.forum.post

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hyfish.app.R
import com.hyfish.app.data.api.PostItem
import com.hyfish.app.databinding.ActivityPostDetailBinding
import com.hyfish.app.view.ViewModelFactory

@Suppress("DEPRECATION")
class PostDetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<PostDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val post = intent.getParcelableExtra<PostItem>(EXTRA_POST)

        if (post != null) {
            if (post.images.isNotEmpty()) {
                binding.ivItemPhoto.visibility = View.VISIBLE
                Glide.with(binding.root.context).load(post.images[0]).into(binding.ivItemPhoto)
            } else {
                binding.ivItemPhoto.visibility = View.GONE
            }

            binding.tvItemTitle.text = post.title
            binding.tvItemDate.text = post.createdAt
            binding.tvItemUsername.text = post.author
            binding.tvItemBody.text = post.body
            updateCounters(post)

            binding.rvComments.layoutManager = LinearLayoutManager(this)
            val commentAdapter = CommentAdapter()
            binding.rvComments.adapter = commentAdapter

            commentAdapter.submitList(post.comments)

            binding.btLike.setOnClickListener {
//                TODO: like post
            }

            binding.btComment.setOnClickListener {
                binding.inComment.requestFocus()
            }

            viewModel.newComment.observe(this) {
                it.getContentIfNotHandled()?.let { comment ->
                    val currentList = commentAdapter.currentList.toMutableList()
                    currentList.add(comment)
                    post.comments = currentList
                    commentAdapter.submitList(currentList)
                    updateCounters(post)
                }
            }

            binding.fabSend.setOnClickListener {
                val message = binding.inComment.text.toString()
                if (message.isNotEmpty()) {
                    viewModel.createComment(post.id, message)
                    binding.inComment.text?.clear()
                    binding.inComment.clearFocus()
                }
            }
        }
    }

    private fun updateCounters(post: PostItem) {
        binding.tvItemLikes.text = getString(R.string.item_likes, post.likes)
        binding.tvItemComments.text = getString(R.string.item_comments, post.comments.size)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_POST = "post"
    }
}
