package com.vaidyakhil.newsfeed.listing

import com.vaidyakhil.newsfeed.listing.model.NewsFeed

interface NewsFeedContract {
    interface View {
        fun showLoader()
        fun hideLoader()
        fun updateNewsFeed()
        fun navigateToFullArticle(articleLink: String)
        fun showToast(message: String)
    }

    interface ListPresenter {
        fun fetchNewsFeed()
        fun detach()
    }

    interface ItemView {
        fun setTitle(title: String)
        fun setImage(imageUrl: String)
        fun setClickHandler(handler: () -> Unit)
    }

    interface ItemPresenter {
        fun setNewsFeedItem(holder: ItemView, index: Int)
        fun getNewsFeedLength(): Int
    }

    interface DataEventCallback {
        fun onSuccess(list: ArrayList<NewsFeed>)
        fun onError(errorMessage: String?)
    }

    interface DataManager {
        fun loadData(callback: DataEventCallback)
    }
}