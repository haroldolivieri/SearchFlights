package com.haroldo.searchforflights.flightsresults.presentation

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.haroldo.searchforflights.diffCallback
import com.haroldo.searchforflights.flightsresults.interactor.SearchFlightsInteractor
import com.haroldo.searchforflights.model.Itinerary
import com.haroldo.searchforflights.request.Event
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject


class FlightsResultPresenter @Inject constructor(
    private val interactor: SearchFlightsInteractor
) {

    private var view: FlightsResultView? = null

    private val disposables = CompositeDisposable()

    fun onAttach(view: FlightsResultView) {
        this.view = view

        val events = interactor.events().share()

        with(events) {
            subscribeToLoadingEvents(filter { it.isLoading })
            subscribeToErrorEvents(filter { it.isError })
            subscribeToDataArrivedEvents(filter { it.hasData })
            subscribeToCompletedEvents(filter { it.isCompletedWithData })
        }
    }

    fun retry() {
        interactor.retry()
    }

    fun onDettach() {
        this.view = null
        disposables.clear()
    }

    private fun subscribeToLoadingEvents(events: Observable<Event<List<Itinerary>>>) {
        events
            .subscribe { view?.showLoading() }
            .addTo(disposables)
    }

    @SuppressLint("CheckResult")
    private fun subscribeToErrorEvents(events: Observable<Event<List<Itinerary>>>) {
        view?.run {
            events.subscribe {
                hideLoading()
                showGenericError()
            }.addTo(disposables)
        }
    }

    private fun subscribeToDataArrivedEvents(events: Observable<Event<List<Itinerary>>>) {
        events.renderListWithDiff()
    }

    private fun subscribeToCompletedEvents(events: Observable<Event<List<Itinerary>>>) {
        events
            .doOnNext { view?.hideLoading() }
            .renderListWithDiff()
    }

    @SuppressLint("CheckResult")
    private fun Observable<Event<List<Itinerary>>>.renderListWithDiff() {
        map { it.data!! }
            .diffCallback { oldItems, newItems ->
                ItinerariesDiffCallback(oldItineraries = oldItems, newItineraries = newItems)
            }
            .subscribe { (newItems, diffResult) ->
                view?.updateItems(newItems, diffResult!!)
            }.addTo(disposables)
    }
}

interface FlightsResultView {
    fun showLoading()
    fun hideLoading()
    fun showGenericError()
    fun updateItems(newItems: List<Itinerary>, diffResult: DiffUtil.DiffResult)
    //recyclerViewAdapter.updateItems(pair.items);
    //pair.diffResult.dispatchUpdatesTo(recyclerViewAdapter);
}