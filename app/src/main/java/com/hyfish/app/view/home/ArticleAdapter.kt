package com.hyfish.app.view.home

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyfish.app.data.api.ArticleItem
import com.hyfish.app.databinding.ItemArticleBinding

class ArticleAdapter : ListAdapter<ArticleItem, ArticleAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    class ItemViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArticleItem) {
            Glide.with(binding.root.context).load(item.images[0]).into(binding.ivItemPhoto)
            binding.tvItemTitle.text = item.title
            binding.tvItemDescription.text =
                if (item.body.length > 100) item.body.substring(0, 100) else item.body

            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, DetailActivity::class.java)
//                intent.putExtra(DetailActivity.EXTRA_ARTICLE, item)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "photo"),
                        Pair(binding.tvItemTitle, "title"),
                        Pair(binding.tvItemDescription, "desc"),
                    )

//                startActivity(itemView.context, intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleItem>() {
            override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
