package com.hyfish.app.view.fishes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyfish.app.data.api.FishItem
import com.hyfish.app.databinding.ItemFishBinding

class FishesAdapter : ListAdapter<FishItem, FishesAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: FishItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemFishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class ItemViewHolder(private val binding: ItemFishBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FishItem){
            if (item.images.isNotEmpty() == true) {
                Glide.with(binding.root.context)
                    .load(item.images[0])
                    .into(binding.ivItemPhoto)
            }
            binding.tvItemTitle.text = item.name
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FishItem>() {
            override fun areItemsTheSame(oldItem: FishItem, newItem: FishItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: FishItem, newItem: FishItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
