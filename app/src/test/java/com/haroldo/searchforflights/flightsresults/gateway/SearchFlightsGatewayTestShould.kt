package com.haroldo.searchforflights.flightsresults.gateway

import com.haroldo.searchforflights.network.PollingUrlProvider
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test

private const val POLLING_URL = "pollingURL"
private const val API_KEY = "api_key"

class SearchFlightsGatewayTestShould {

    private val api: SearchFlightsGateway.Api = mock()
    private val provider: PollingUrlProvider = mock {
        on { get() }.thenReturn(POLLING_URL)
    }

    private val gateway = SearchFlightsGateway(api, API_KEY, provider)

    @Test
    fun `get data for provider when fetch results called`() {
        gateway.fetchSearchFlightsResult(pageSize = 5, pageIndex = 0)

        verify(provider).get()
        verify(api).poolingResults(
            POLLING_URL,
            apiKey = API_KEY,
            pageSize = 5,
            pageIndex = 0,
            sortOrder = "asc",
            sortType = "price"
        )
    }
}