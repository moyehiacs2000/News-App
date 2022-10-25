package com.moyehics.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.moyehics.news.R
import com.moyehics.news.data.model.OnBordingData

class OnBordiingViewPagerAdapter(private var context: Context,private var onBordingDataList : List<OnBordingData>) : PagerAdapter() {
    override fun getCount(): Int {
        return onBordingDataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view= LayoutInflater.from(context).inflate(R.layout.onbording_screen_item,null)
        val imageView:ImageView
        val title:TextView
        val desc:TextView
        imageView  = view.findViewById(R.id.image)
        title  = view.findViewById(R.id.txtTitle)
        desc  = view.findViewById(R.id.txtDesc)
        imageView.setImageResource(onBordingDataList[position].imageUrl)
        title.text = onBordingDataList[position].title
        desc.text = onBordingDataList[position].desc
        container.addView(view)
        return view
    }
}