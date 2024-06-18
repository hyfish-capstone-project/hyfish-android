package com.hyfish.app.view.forum.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hyfish.app.data.api.CommentsItem
import com.hyfish.app.databinding.ItemCommentBinding

class CommentAdapter : ListAdapter<CommentsItem, CommentAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: CommentsItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class ItemViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommentsItem) {
            binding.tvItemUsername.text = item.author.username
            binding.tvItemMessage.text = item.message
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CommentsItem>() {
            override fun areItemsTheSame(oldItem: CommentsItem, newItem: CommentsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CommentsItem, newItem: CommentsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
