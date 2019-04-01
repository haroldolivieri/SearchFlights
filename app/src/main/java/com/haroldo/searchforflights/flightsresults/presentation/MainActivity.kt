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

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showGenericError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateItems(newItems: List<Itinerary>, diffResult: DiffUtil.DiffResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun performInjections() {
        FlightsResultComponentHolder.getComponent(this).inject(this)
    }
}
