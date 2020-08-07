package com.haroldo.searchforflights.flightsresults.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haroldo.searchforflights.R
import com.haroldo.searchforflights.changeVisibility
import com.haroldo.searchforflights.di.FlightsResultComponentHolder
import com.haroldo.searchforflights.flightsresults.presentation.adapter.FlightsResultAdapter
import com.haroldo.searchforflights.model.Itinerary
import com.haroldo.searchforflights.showSnackbar
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), FlightsResultView {

    @Inject
    lateinit var presenter: FlightsResultPresenter

    @Inject
    lateinit var resultsAdapter: FlightsResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        performInjections()

        presenter.onAttach(this)

        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = resultsAdapter
            addOnScrollListener(onScrollListener)
        }
    }

    private fun performInjections() {
        FlightsResultComponentHolder.getComponent(this).inject(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ITEMS_COUNT_TEXT, resultsCounter.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        resultsCounter.text = savedInstanceState?.getString(ITEMS_COUNT_TEXT)
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun showLoading() {
        loading.changeVisibility(true)
    }

    override fun hideLoading() {
        loading.changeVisibility(false)
    }

    override fun showCreateSessionError() {
        recyclerView.showSnackbar("Create session error", R.string.retry) {
            presenter.retrySession()
        }
    }

    override fun showFetchResultsError() {
        recyclerView.showSnackbar("Fetch results error", R.string.retry) {
            presenter.retryFetchResults()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun showResultsCount(count: Int) {
        resultsCounter.text = "$count results"
    }

    override fun updateItems(newItems: List<Itinerary?>) {
        resultsAdapter.submitList(newItems)
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
            val endHasBeenReached = lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount

            if (totalItemCount > 0 && endHasBeenReached) {
                presenter.endScrollHasBeenReached()
            }
        }
    }

    companion object {
        private const val VISIBLE_THRESHOLD = 1
        private const val ITEMS_COUNT_TEXT = "itemsCounter"
    }
}