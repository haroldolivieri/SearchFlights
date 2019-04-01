package com.haroldo.searchforflights.flightsresults.gateway

import com.haroldo.searchforflights.network.PollingUrlProvider
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

private const val POLLING_URL = "pollingURL"

class SearchFlightsGatewayTestShould {
    private val api: SearchFlightsGateway.Api = mock()
    private val provider: PollingUrlProvider = mock {
        on { get() }.thenReturn(POLLING_URL)
    }
    private val gateway = SearchFlightsGateway(api, provider)

    @Test
    fun `get data for provider when fetch results called`() {
        gateway.fetchSearchFlightsResult()

        verify(provider).get()
        verify(api).poolingResults(POLLING_URL)
    }
}