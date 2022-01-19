package com.vaidyakhil.newsfeed.listing

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vaidyakhil.newsfeed.R
import com.vaidyakhil.newsfeed.listing.model.NewsFeedDataManagerImpl
import com.vaidyakhil.newsfeed.listing.presenter.NewsFeedPresenterImpl
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.DividerItemDecoration




class ListingActivity : AppCompatActivity(), NewsFeedContract.View {

    private lateinit var newsFeedRecyclerView: RecyclerView
    private lateinit var swipeRefreshContainer: SwipeRefreshLayout
    private lateinit var loadMoreButton: Button

    private val newsFeedDataManager: NewsFeedContract.DataManager = NewsFeedDataManagerImpl(this)
    private val newsFeedListPresenter by lazy {
        NewsFeedPresenterImpl(this, newsFeedDataManager)
    }

    private lateinit var adapter: NewsFeedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
        loadData()
    }

    private fun initialize() {
        swipeRefreshContainer = findViewById(R.id.swipe_refresh_container)
        loadMoreButton = findViewById(R.id.load_more_button)

        configureNewsList()
        configurePullToRefresh()
        configureLoadMoreButton()
    }

    private fun configureLoadMoreButton() {
        loadMoreButton.visibility = View.INVISIBLE
        loadMoreButton.setOnClickListener {
            loadData()
        }
    }

    private fun configurePullToRefresh() {
        swipeRefreshContainer.setOnRefreshListener {
            swipeRefreshContainer.isRefreshing = true
            loadData()
        }
    }

    private fun configureNewsList() {
        newsFeedRecyclerView = findViewById(R.id.news_feed_list)
        adapter = NewsFeedListAdapter(newsFeedListPresenter)
        newsFeedRecyclerView.layoutManager = LinearLayoutManager(this)
        newsFeedRecyclerView.adapter = adapter

        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.item_divider)!!)
        newsFeedRecyclerView.addItemDecoration(itemDecorator)

        newsFeedRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    loadMoreButton.visibility = View.VISIBLE
                } else {
                    loadMoreButton.visibility = View.GONE
                }
            }
        })
    }

    private fun loadData() {
        newsFeedListPresenter.fetchNewsFeed()
    }

    override fun showLoader() {
        if (swipeRefreshContainer.isRefreshing) {
            return
        }
        findViewById<ProgressBar>(R.id.progress_Bar).visibility = View.VISIBLE
    }

    override fun hideLoader() {
        if (swipeRefreshContainer.isRefreshing) {
            return
        }
        findViewById<ProgressBar>(R.id.progress_Bar).visibility = View.GONE
    }

    override fun navigateToFullArticle(articleLink: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(articleLink))
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun updateNewsFeed() {
        adapter.notifyDataSetChanged()
        newsFeedRecyclerView.scrollToPosition(0)
        removeRefreshProgressBar()
    }

    private fun removeRefreshProgressBar() {
        swipeRefreshContainer.isRefreshing = false
    }
}