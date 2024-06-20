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
import com.hyfish.app.util.toReadableDate

class PostAdapter : ListAdapter<PostItem, PostAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null
    private var onLikeClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: PostItem)
    }

    class ItemViewHolder(
        private val binding: ItemPostBinding,
        private var onLikeClickCallback: OnItemClickCallback
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostItem) {
            if (item.images.isNotEmpty()) {
                binding.ivItemPhoto.visibility = View.VISIBLE
                Glide.with(binding.root.context).load(item.images[0]).into(binding.ivItemPhoto)
            } else {
                binding.ivItemPhoto.visibility = View.GONE
            }
            binding.tvItemUsername.text = item.title
            binding.tvItemDate.text = item.createdAt.toReadableDate()
            binding.tvItemBody.text =
                if (item.body.length > 200) item.body.substring(0, 200) else item.body
            binding.tvItemLikes.text =
                binding.root.context.getString(R.string.item_likes, item.likes)
            binding.tvItemComments.text =
                binding.root.context.getString(R.string.item_comments, item.comments.size)

            binding.btLike.setCompoundDrawablesWithIntrinsicBounds(
                if (item.isLiked) R.drawable.ic_thumb_up else R.drawable.ic_thumb_up_off,
                0,
                0,
                0
            )

            binding.btLike.setOnClickListener {
                onLikeClickCallback.onItemClicked(item)
            }

            binding.btComment.setOnClickListener {
                itemView.performClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, onLikeClickCallback!!)
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

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setOnLikeClickCallback(onLikeClickCallback: OnItemClickCallback) {
        this.onLikeClickCallback = onLikeClickCallback
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
