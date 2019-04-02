package com.haroldo.searchforflights.flightsresults.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import com.haroldo.searchforflights.R
import com.haroldo.searchforflights.di.FlightsResultComponentHolder
import com.haroldo.searchforflights.model.Itinerary
import javax.inject.Inject

class MainActivity : AppCompatActivity(), FlightsResultView {

    @Inject
    lateinit var presenter: FlightsResultPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        performInjections()

        presenter.onAttach(this)
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showCreateSessionError() {
    }

    override fun showFetchResultsError() {
    }

    override fun updateItems(newItems: List<Itinerary>, diffResult: DiffUtil.DiffResult) {
        //recyclerViewAdapter.updateItems(pair.items);
        //pair.diffResult.dispatchUpdatesTo(recyclerViewAdapter);
    }

    private fun performInjections() {
        FlightsResultComponentHolder.getComponent(this).inject(this)
    }
}
