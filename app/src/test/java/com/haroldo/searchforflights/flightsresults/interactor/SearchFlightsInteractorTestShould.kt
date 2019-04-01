package com.haroldo.searchforflights.flightsresults.interactor

import com.haroldo.searchforflights.flightsresults.RxTestRule
import com.haroldo.searchforflights.flightsresults.gateway.SearchFlightsGateway
import com.haroldo.searchforflights.model.api.ApiResponseSearch
import com.haroldo.searchforflights.model.api.Status
import com.haroldo.searchforflights.model.mapper.ItineraryResponseMapper
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit


class SearchFlightsInteractorTestShould {

    @Rule
    @JvmField
    val rule = RxTestRule()

    private val gateway: SearchFlightsGateway = mock {
        on { fetchSearchFlightsResult() }.thenReturn(Single.fromCallable { apiResponse })
    }
    private val mapper: ItineraryResponseMapper = mock {
        on { map(any()) }.thenReturn(emptyList())
    }

    private var apiResponse: ApiResponseSearch = givenResponse()

    private val testScheduler = TestScheduler()
    private var interactor = SearchFlightsInteractor(gateway, mapper, testScheduler)

    @Test
    fun `do not map data when is not subscribed`() {
        verify(mapper, never()).map(any())
    }

    @Test
    fun `map received data after subscription`() {
        interactor.events().test()

        verify(mapper).map(any())
    }

    @Test
    fun `call gateway once per second when status is pending`() {
        interactor.events().test()
        reset(mapper)

        testScheduler.advanceTimeBy(10, TimeUnit.SECONDS)
        verify(mapper, times(10)).map(any())
        reset(mapper)
    }

    @Test
    fun `call mapper just once after status is complete`() {
        interactor.events().test()
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