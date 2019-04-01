package com.haroldo.searchforflights.flightsresults.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.haroldo.searchforflights.R
import com.haroldo.searchforflights.di.FlightsResultComponentHolder
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

    private fun performInjections() {
        FlightsResultComponentHolder.getComponent(this).inject(this)
    }

}
