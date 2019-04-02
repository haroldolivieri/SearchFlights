package com.haroldo.searchforflights.flightsresults.interactor

import com.haroldo.searchforflights.flightsresults.RxTestRule
import com.haroldo.searchforflights.flightsresults.gateway.SearchFlightsGateway
import com.haroldo.searchforflights.model.SearchQuery
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import org.junit.Rule
import org.junit.Test

class CreateSessionInteractorTestShould {

    @Rule
    @JvmField
    val rule = RxTestRule()

    private val subject = CompletableSubject.create()
    private val gateway: SearchFlightsGateway = mock {
        on { createSearchFlightsSession(any()) }.thenReturn(subject)
    }
    private val interactor = CreateSessionInteractor(gateway)

    @Test
    fun `call gateway when interact events is called`() {
        subject.onComplete()

        interactor.events(firstQuery).test()
        verify(gateway).createSearchFlightsSession(firstQuery)
    }

    @Test
    fun `do not call gateway again on a second subscription if the query is the same`() {
        subject.onComplete()

        interactor.events(firstQuery).test()
        reset(gateway)

        interactor.events(firstQuery.copy()).test()
        verify(gateway, never()).createSearchFlightsSession(any())
    }

    @Test
    fun `call gateway again on a second subscription if the query is not the same`() {
        subject.onComplete()

        interactor.events(firstQuery).test()
        reset(gateway)

        whenever(gateway.createSearchFlightsSession(secondQuery)).thenReturn(Completable.complete())

        interactor.events(secondQuery).test()
        verify(gateway).createSearchFlightsSession(secondQuery)
    }

    private val firstQuery = SearchQuery(
        outboundDate = "2015-11-30",
        inboundDate = "2015-12-01"
    )

    private val secondQuery = SearchQuery(
        outboundDate = "2015-11-31",
        inboundDate = "2015-12-01"
    )
}