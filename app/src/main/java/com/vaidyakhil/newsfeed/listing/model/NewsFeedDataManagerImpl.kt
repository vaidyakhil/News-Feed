package com.vaidyakhil.newsfeed.listing.model

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.vaidyakhil.newsfeed.listing.NewsFeedContract
import com.vaidyakhil.newsfeed.network.NetworkOperationManager

class NewsFeedDataManagerImpl(private val context: Context): NewsFeedContract.DataManager {

    companion object {
        const val BASE_URL = "https://newsdata.io/"
        const val END_POINT = "api/1/news"
        const val API_KEY = "pub_374793a1073724604452e1350a849ca0e5b1"
        const val COUNTRY_CODE = "in"
    }

    private var page = 0

    private fun getNewsFeedRequest(callback: NewsFeedContract.DataEventCallback): JsonObjectRequest{
        val query = "?apikey=$API_KEY&country=$COUNTRY_CODE&page=$page"
        val url = "${BASE_URL}$END_POINT$query"

        page+=1

        return JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val newsJsonArray = it.getJSONArray("results")
                val newsArray = ArrayList<NewsFeed>()
                for(i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = NewsFeed(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("image_url"),
                        newsJsonObject.getString("link"),
                    )
                    newsArray.add(news)
                }

                page = it.optInt("nextPage") ?: page+1

                callback.onSuccess(newsArray)
            },
            {
                callback.onError(it.message)
            })
    }

    override fun loadData(callback: NewsFeedContract.DataEventCallback) {
        NetworkOperationManager.getInstance(context).addToRequestQueue(getNewsFeedRequest(callback))
    }
}