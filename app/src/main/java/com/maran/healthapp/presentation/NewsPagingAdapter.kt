package com.maran.healthapp.presentation

// для примера, тк у нас тут другие элементы вьюшки

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maran.healthapp.R
import com.maran.healthapp.domain.models.ArticleModel

/*class NewsPagingAdapter(
    private val onItemClick: (ArticleModel) -> Unit,
    private val onReadFullClick: (ArticleModel) -> Unit
) : PagingDataAdapter<ArticleModel, NewsPagingAdapter.ArticleModelViewHolder>(DiffCallback) {

    inner class ArticleModelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(news: ArticleModel) {
            itemView.apply {
                findViewById<TextView>(R.id.tvTitle).text = news.title
                findViewById<TextView>(R.id.description).text = news.description ?: ""
                findViewById<TextView>(R.id.author).text = news.author ?: ""
                val btnReadFull: Button = findViewById(R.id.btnReadFull)

                Glide.with(context)
                    .load(news.urlToImage)
                    .into(findViewById(R.id.newsImage))

                setOnClickListener { onItemClick(news) }
                btnReadFull.setOnClickListener { onReadFullClick(news) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleModelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_news_details, parent, false)
        return ArticleModelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleModelViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel) =
                oldItem.url == newItem.url
            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel) =
                oldItem == newItem
        }
    }
}*/