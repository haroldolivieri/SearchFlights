package com.haroldo.searchforflights.flightsresults.presentation

import com.haroldo.searchforflights.flightsresults.interactor.SearchFlightsInteractor
import javax.inject.Inject

class FlightsResultPresenter @Inject constructor(
    private val interactor: SearchFlightsInteractor
) {

    private var view: FlightsResultView? = null

    fun onAttach(view: FlightsResultView) {
        this.view = view
        interactor.events()
    }
}

interface FlightsResultView