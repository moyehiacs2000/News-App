package com.moyehics.news.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moyehics.news.R
import com.moyehics.news.data.model.news.Article
import com.moyehics.news.util.Utils
import com.squareup.picasso.Picasso

class NewsAdapter(val context:Context): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private val differCallBack = object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.news_card_item,parent,false)
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article=differ.currentList[position]
        holder.setDat(article,position)
        holder.itemView.setOnClickListener {
            onItemClickedListener?.let { it(article) }
        }
    }

    override fun getItemCount(): Int =differ.currentList.size

    inner class NewsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        private var currentPosition:Int = -1
        private var currentArticle : Article?=null
        private val imvNews=itemView.findViewById<ImageView>(R.id.news_image)
        private val txvAuther=itemView.findViewById<TextView>(R.id.txv_auther)
        private val txvTitle=itemView.findViewById<TextView>(R.id.txv_title)
        private val txvSource=itemView.findViewById<TextView>(R.id.txv_source)
        private val txvTime=itemView.findViewById<TextView>(R.id.txv_time)

        fun setDat(article : Article,position: Int){
            this.currentPosition=position
            this.currentArticle=article
            /*Picasso.get()
                .load(article.urlToImage)
                .into(imvNews)*/
            Glide.with(context).load(article.urlToImage).into(imvNews)
            txvAuther.text = article.author
            txvSource.text = article.source?.name
            txvTitle.text = article.title
            Log.d("NewsAdapter",article.title)
            txvTime.text = Utils.DateToTimeFormat(article.publishedAt)
        }
    }
    private var onItemClickedListener:((Article) -> Unit)?=null

    fun setOnItemClicListener(listener:(Article) -> Unit){
        onItemClickedListener=listener
    }
}