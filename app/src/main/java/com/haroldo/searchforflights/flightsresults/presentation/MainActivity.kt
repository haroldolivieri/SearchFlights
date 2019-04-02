package com.haroldo.searchforflights.flightsresults.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.haroldo.searchforflights.R
import com.haroldo.searchforflights.changeVisibility
import com.haroldo.searchforflights.di.FlightsResultComponentHolder
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
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = resultsAdapter
        }
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

    override fun updateItems(newItems: List<Itinerary>, diffResult: DiffUtil.DiffResult) {
        resultsAdapter.updateItems(newItems, diffResult) {
            recyclerView.showSnackbar("Feature not implemented yet")
        }
    }

    private fun performInjections() {
        FlightsResultComponentHolder.getComponent(this).inject(this)
    }
}