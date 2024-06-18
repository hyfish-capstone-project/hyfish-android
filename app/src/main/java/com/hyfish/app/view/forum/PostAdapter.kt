package com.hyfish.app.view.forum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyfish.app.R
import com.hyfish.app.data.api.PostItem
import com.hyfish.app.databinding.ItemPostBinding

class PostAdapter : ListAdapter<PostItem, PostAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: PostItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
        if (onItemClickCallback != null) {
            holder.itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(data)
            }
        }
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
            binding.tvItemDate.text = item.createdAt
            binding.tvItemBody.text = if (item.body.length > 200) item.body.substring(0, 200) else item.body
            binding.tvItemLikes.text = binding.root.context.getString(R.string.item_likes, item.likes)
            binding.tvItemComments.text = binding.root.context.getString(R.string.item_comments, item.comments.size)

            binding.btLike.setOnClickListener {
//                TODO: like post
            }

            binding.btComment.setOnClickListener {
                itemView.performClick()
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
