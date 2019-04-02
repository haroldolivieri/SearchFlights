package com.haroldo.searchforflights.flightsresults.presentation

import androidx.recyclerview.widget.DiffUtil
import com.haroldo.searchforflights.diffCallback
import com.haroldo.searchforflights.flightsresults.interactor.CreateSessionInteractor
import com.haroldo.searchforflights.flightsresults.interactor.SearchFlightsInteractor
import com.haroldo.searchforflights.model.Itinerary
import com.haroldo.searchforflights.model.SearchQuery
import com.haroldo.searchforflights.request.reducer
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTimeConstants
import org.joda.time.LocalDate
import javax.inject.Inject


private const val DATE_FORMAT = "yyyy-MM-dd"

class FlightsResultPresenter @Inject constructor(
    private val resultsInteractor: SearchFlightsInteractor,
    private val sessionInteractor: CreateSessionInteractor
) {

    private var view: FlightsResultView? = null

    private val disposables = CompositeDisposable()

    fun onAttach(view: FlightsResultView) {
        this.view = view

        sessionInteractor.events(createMockedSearchQuery()).subscribe {
            view.run {
                it.reducer(
                    onLoading = { showLoading() },
                    onError = {
                        hideLoading()
                        showCreateSessionError()
                    },
                    onCompleted = { fetchResults() }
                )
            }
        }.addTo(disposables)
    }

    fun retrySession() {
        sessionInteractor.retry()
    }

    fun retryFetchResults() {
        resultsInteractor.retry()
    }

    fun onDetach() {
        this.view = null
        disposables.clear()
    }

    private fun fetchResults() {
        resultsInteractor.events().subscribe { event ->
            view?.run {
                event.reducer(
                    onLoading = { showLoading() },
                    onError = {
                        hideLoading()
                        showFetchResultsError()
                    },
                    onDataArrived = { calculateDiff(event.data!!) },
                    onCompleted = {
                        hideLoading()
                        calculateDiff(event.data!!)
                    }
                )
            }
        }.addTo(disposables)
    }

    private fun calculateDiff(data: List<Itinerary>) {
        Observable.just(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .diffCallback { oldItems, newItems ->
                ItinerariesDiffCallback(oldItineraries = oldItems, newItineraries = newItems)
            }.subscribe { (newItems, diffResult) ->
                view?.updateItems(newItems, diffResult!!)
            }.addTo(disposables)
    }

    private fun createMockedSearchQuery(): SearchQuery {

        val today = LocalDate.now()
        val old = today.dayOfWeek
        val nextMonday = today.plusDays(8 - old)

        val outboundDate = nextMonday.toString(DATE_FORMAT)
        val inboundDate = nextMonday.plusDays(1).toString(DATE_FORMAT)

        return SearchQuery(
            outboundDate = "2019-04-03",
            inboundDate = "2019-04-04"
        )
    }
}

interface FlightsResultView {
    fun showLoading()
    fun hideLoading()
    fun showCreateSessionError()
    fun showFetchResultsError()
    fun updateItems(newItems: List<Itinerary>, diffResult: DiffUtil.DiffResult)
}