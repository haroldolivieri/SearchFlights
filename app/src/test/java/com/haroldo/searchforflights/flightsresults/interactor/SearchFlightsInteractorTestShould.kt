package com.haroldo.searchforflights.flightsresults.interactor

import com.haroldo.searchforflights.flightsresults.RxTestRule
import com.haroldo.searchforflights.flightsresults.model.Itinerary
import com.haroldo.searchforflights.flightsresults.model.api.ApiResponseSearch
import com.haroldo.searchforflights.flightsresults.model.api.SearchFlightsApiGateway
import com.haroldo.searchforflights.flightsresults.model.api.Status
import com.haroldo.searchforflights.flightsresults.model.mapper.ItineraryResponseMapper
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit
import io.reactivex.SingleEmitter
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.SingleSubject
import org.junit.After

class SearchFlightsInteractorTestShould {

    private val testScheduler = TestScheduler()

    @Rule
    @JvmField
    val rule = RxTestRule(testScheduler)

    private val gateway: SearchFlightsApiGateway = mock {
        on { fetchSearchFlightsResult() }.thenReturn(Single.fromCallable { apiResponse })
    }
    private val mapper: ItineraryResponseMapper = mock {
        on { map(any()) }.thenReturn(emptyList())
    }

    private var apiResponse: ApiResponseSearch = givenResponse()

    private var interactor = SearchFlightsInteractor(gateway, mapper)

    @Before
    fun setup() {
        interactor = SearchFlightsInteractor(gateway, mapper)
    }

    @Test
    fun `does not receive mapped data from interactor`() {
        verify(mapper, never()).map(any())
    }

    @Test
    fun `call gateway once per second when status is pending`() {
        interactor.events().subscribe()
        reset(mapper)

        testScheduler.advanceTimeBy(10, TimeUnit.SECONDS)
        verify(mapper, times(10)).map(any())
        reset(mapper)

        apiResponse = givenResponse(Status.UpdatesComplete)
        testScheduler.advanceTimeBy(100, TimeUnit.SECONDS)
        verify(mapper).map(any())
    }

    private companion object {
        fun givenResponse(status: Status = Status.UpdatesPending) = ApiResponseSearch(
            status = status,
            itineraries = emptyList()
        )
    }
}