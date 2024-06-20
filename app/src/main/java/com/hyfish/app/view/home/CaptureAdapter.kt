package com.hyfish.app.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyfish.app.data.api.CaptureItemWithFish
import com.hyfish.app.databinding.ItemCaptureBinding
import com.hyfish.app.util.toReadableDate
import com.hyfish.app.view.scan.ScanActivity

class CaptureAdapter :
    ListAdapter<CaptureItemWithFish, CaptureAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: CaptureItemWithFish)
    }

    class ItemViewHolder(private val binding: ItemCaptureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CaptureItemWithFish) {
            Glide.with(binding.root.context).load(item.imageUrl).into(binding.ivItemPhoto)
            when (item.type) {
                ScanActivity.ScanType.FRESHNESS.value -> {
                    binding.tvItemTitle.text = item.freshness
                }

                ScanActivity.ScanType.CLASSIFICATION.value -> {
                    binding.tvItemTitle.text = item.fish?.name ?: "Unknown"
                }

                else -> {
                    throw IllegalArgumentException("Unknown scan type")
                }
            }
            binding.tvItemDescription.text = item.type
            binding.tvItemDatetime.text = item.createdAt.toReadableDate()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemCaptureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CaptureItemWithFish>() {
            override fun areItemsTheSame(
                oldItem: CaptureItemWithFish, newItem: CaptureItemWithFish
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CaptureItemWithFish, newItem: CaptureItemWithFish
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
