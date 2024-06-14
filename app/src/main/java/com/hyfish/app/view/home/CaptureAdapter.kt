package com.hyfish.app.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyfish.app.data.api.CaptureItem
import com.hyfish.app.databinding.ItemCaptureBinding
import com.hyfish.app.view.scan.ScanActivity

class CaptureAdapter : ListAdapter<CaptureItem, CaptureAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemCaptureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
    }

    class ItemViewHolder(private val binding: ItemCaptureBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CaptureItem){
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .into(binding.ivItemPhoto)
            when (item.type) {
                ScanActivity.ScanType.FRESHNESS.value -> {
                    binding.tvItemTitle.text = item.freshness
                }
                ScanActivity.ScanType.CLASSIFICATION.value -> {
                    binding.tvItemTitle.text = item.fishId.toString()
                }
                else -> {
                    throw IllegalArgumentException("Unknown scan type")
                }
            }
//            binding.tvItemDescription.text = item.result
            binding.tvItemDatetime.text = item.createdAt

            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, DetailActivity::class.java)
//                intent.putExtra(DetailActivity.EXTRA_ARTICLE, item)

//                val optionsCompat: ActivityOptionsCompat =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        itemView.context as Activity,
//                        Pair(binding.ivItemPhoto, "photo"),
//                        Pair(binding.tvItemTitle, "title"),
//                        Pair(binding.tvItemDescription, "desc"),
//                    )

//                startActivity(itemView.context, intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CaptureItem>() {
            override fun areItemsTheSame(oldItem: CaptureItem, newItem: CaptureItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: CaptureItem, newItem: CaptureItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
