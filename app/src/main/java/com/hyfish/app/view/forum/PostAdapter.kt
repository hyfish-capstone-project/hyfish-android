package com.hyfish.app.view.forum

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyfish.app.R
import com.hyfish.app.data.api.PostItem
import com.hyfish.app.databinding.ItemPostBinding
import com.hyfish.app.view.forum.post.PostDetailActivity

class PostAdapter : ListAdapter<PostItem, PostAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
    }

    class ItemViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostItem){
            if (item.images.isNotEmpty()) {
                binding.ivItemPhoto.visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .load(item.images[0])
                    .into(binding.ivItemPhoto)
            } else {
                binding.ivItemPhoto.visibility = View.GONE
            }
            binding.tvItemUsername.text = item.title
            binding.tvItemBody.text = if (item.body.length > 200) item.body.substring(0, 200) else item.body
            binding.tvItemLikes.text = binding.root.context.getString(R.string.item_likes, item.likes)
            binding.tvItemComments.text = binding.root.context.getString(R.string.item_comments, item.comments.size)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PostDetailActivity::class.java)
                intent.putExtra(PostDetailActivity.EXTRA_POST_ID, 1)

//                val optionsCompat: ActivityOptionsCompat =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        itemView.context as Activity,
//                        Pair(binding.ivItemPhoto, "photo"),
//                        Pair(binding.tvItemTitle, "title"),
//                        Pair(binding.tvItemDescription, "desc"),
//                    )
//                startActivity(itemView.context, intent, optionsCompat.toBundle())

                startActivity(itemView.context, intent, null)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostItem>() {
            override fun areItemsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
