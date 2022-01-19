package com.vaidyakhil.newsfeed.listing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vaidyakhil.newsfeed.R

import com.bumptech.glide.request.RequestOptions




class NewsFeedListAdapter(private val itemPresenter: NewsFeedContract.ItemPresenter): RecyclerView.Adapter<NewsFeedViewItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFeedViewItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_feed, parent, false)
        return NewsFeedViewItemHolder(view)
    }

    override fun onBindViewHolder(itemHolder: NewsFeedViewItemHolder, position: Int) {
        itemPresenter.setNewsFeedItem(itemHolder, position)
    }

    override fun getItemCount(): Int {
        return itemPresenter.getNewsFeedLength()
    }
}

class NewsFeedViewItemHolder(itemView: View): RecyclerView.ViewHolder(itemView),
    NewsFeedContract.ItemView {

    private val imageView: ImageView = itemView.findViewById(R.id.item_news_image)
    private val titleView: TextView = itemView.findViewById(R.id.item_news_title)

    override fun setTitle(title: String) {
        titleView.text = title
    }

    override fun setImage(imageUrl: String) {
        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.default_image)
        requestOptions.error(R.drawable.default_image)
        Glide.with(imageView).setDefaultRequestOptions(requestOptions).load(imageUrl).into(imageView);
    }

    override fun setClickHandler(handler: () -> Unit) {
        itemView.setOnClickListener {
            handler.invoke()
        }
    }
}