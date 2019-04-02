package com.haroldo.searchforflights.flightsresults.interactor

import com.haroldo.searchforflights.di.FlightsResultScope
import com.haroldo.searchforflights.flightsresults.gateway.SearchFlightsGateway
import com.haroldo.searchforflights.model.SearchQuery
import com.haroldo.searchforflights.request.Event
import com.haroldo.searchforflights.request.InMemoryCompletableRequest
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

@FlightsResultScope
class CreateSessionInteractor @Inject constructor(
    private val gateway: SearchFlightsGateway
) {
    private var request: InMemoryCompletableRequest? = null
    private var lastQuery: SearchQuery? = null

    fun events(query: SearchQuery): Observable<Event<Unit>> {
        if (request == null || lastQuery != query) {
            this.lastQuery = query
            request?.dispose()
            request = InMemoryCompletableRequest(source(query))
        }

        return request!!.events()
    }

    fun retry() = request?.retry()

    private fun source(query: SearchQuery): Completable = gateway.createSearchFlightsSession(query)
}