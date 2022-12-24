package com.moyehics.news.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
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
        holder.setListeners()
    }

    override fun getItemCount(): Int =differ.currentList.size

    inner class NewsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var currentPosition:Int = -1
        private var currentArticle : Article?=null
        private val imvNews=itemView.findViewById<ImageView>(R.id.news_image)
        private val txvAuther=itemView.findViewById<TextView>(R.id.txv_auther)
        private val txvTitle=itemView.findViewById<TextView>(R.id.txv_title)
        private val txvSource=itemView.findViewById<TextView>(R.id.txv_source)
        private val txvTime=itemView.findViewById<TextView>(R.id.txv_time)
        private val iconSave=itemView.findViewById<ImageView>(R.id.save_icon)
        private val iconShare=itemView.findViewById<ImageView>(R.id.share_icon)
        private val icSaveFilledImage = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_save_filled,null)
        private val icSaveBorderImage = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_save_border,null)
        fun setDat(article : Article,position: Int){
            this.currentPosition=position
            this.currentArticle=article
            Glide.with(context).load(article.urlToImage).placeholder(shimmerDrawable).error(R.drawable.error_image).into(imvNews)
            txvAuther.text = article.author
            txvSource.text = article.source?.name
            txvTitle.text = article.title
            txvTime.text = Utils.DateToTimeFormat(article.publishedAt?:"2022-12-14T17:20:06Z")
            if(article.isSeved){
                iconSave.setImageDrawable(icSaveFilledImage)
            }else{
                iconSave.setImageDrawable(icSaveBorderImage)
            }
        }
        fun setListeners() {
            iconSave.setOnClickListener(this@NewsViewHolder)
            iconShare.setOnClickListener(this@NewsViewHolder)
        }

        override fun onClick(v: View?) {
            when(v!!.id){
                R.id.save_icon -> saveItem()
                R.id.share_icon -> shareItem()
            }
        }

        private fun shareItem() {
            onShareClickedListener?.let { it(currentArticle!!) }
        }

        private fun saveItem() {
            onSaveClickedListener?.let { it(currentArticle!!) }
            currentArticle?.isSeved=!(currentArticle?.isSeved!!)
            if(currentArticle?.isSeved!!){
                iconSave.setImageDrawable(icSaveFilledImage)
            }else{
                iconSave.setImageDrawable(icSaveBorderImage)
            }

        }
    }
    private var onItemClickedListener:((Article) -> Unit)?=null

    fun setOnItemClicListener(listener:(Article) -> Unit){
        onItemClickedListener=listener
    }

    private var onSaveClickedListener:((Article) -> Unit)?=null
    fun setOnSaveClickedListener(listener:(Article) -> Unit){
        onSaveClickedListener = listener
    }

    private var onShareClickedListener:((Article) -> Unit)?=null
    fun setOnShareClickedListener(listener:(Article) -> Unit){
        onShareClickedListener = listener
    }
    private val shimmer = Shimmer.AlphaHighlightBuilder()
        .setDuration(2000).setBaseAlpha(0.9f)
        .setHighlightAlpha(0.8f)
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setAutoStart(true)
        .build()
    val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(shimmer)
    }

}