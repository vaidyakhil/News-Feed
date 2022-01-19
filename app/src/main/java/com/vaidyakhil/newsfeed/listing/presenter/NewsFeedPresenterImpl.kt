package com.vaidyakhil.newsfeed.listing.presenter

import com.vaidyakhil.newsfeed.listing.NewsFeedContract
import com.vaidyakhil.newsfeed.listing.model.NewsFeed

class NewsFeedPresenterImpl(private var mView: NewsFeedContract.View?, private var newsFeedDataManger: NewsFeedContract.DataManager?):
    NewsFeedContract.ListPresenter, NewsFeedContract.ItemPresenter,
    NewsFeedContract.DataEventCallback {

    private var mNewsFeedList = ArrayList<NewsFeed>()

    override fun fetchNewsFeed() {
        mView?.showLoader()
        newsFeedDataManger?.loadData(this)
    }

    override fun detach() {
        mView = null
        newsFeedDataManger = null
    }

    override fun setNewsFeedItem(holder: NewsFeedContract.ItemView, index: Int) {
        if (mNewsFeedList.isNotEmpty() && mNewsFeedList.size > index) {
            holder.setTitle(mNewsFeedList[index].title)
            holder.setImage(mNewsFeedList[index].imageUrl)
            holder.setClickHandler {
                mView?.navigateToFullArticle(mNewsFeedList[index].articleLink)
            }
        }
    }

    override fun getNewsFeedLength(): Int {
       return mNewsFeedList.size
    }


    override fun onSuccess(list: ArrayList<NewsFeed>) {
        mView?.hideLoader()
        mNewsFeedList = list
        mView?.updateNewsFeed()
    }

    override fun onError(errorMessage: String?) {
        mView?.hideLoader()
        errorMessage?.let{
            mView?.showToast(it)
        }
    }
}