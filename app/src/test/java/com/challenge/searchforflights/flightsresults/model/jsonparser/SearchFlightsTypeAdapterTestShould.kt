package com.challenge.searchforflights.flightsresults.model.jsonparser

import com.challenge.searchforflights.flightsresults.model.api.*
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat

@RunWith(JUnitParamsRunner::class)
class SearchFlightsTypeAdapterTestShould {

    @Test
    @Parameters(
        "response_json_completed.json, UpdatesComplete, 967",
        "response_json_pending.json, UpdatesPending, 3"
    )
    fun `parse pending updates json file`(fileName: String, status: Status, amountOfItems: Int) {
        val responseSearch = loadFromFile(fileName)

        assertThat(responseSearch.status).isEqualTo(status)
        assertThat(responseSearch.itineraries?.size).isEqualTo(amountOfItems)
    }

    @Test
    @Parameters(method = "pricingOptions results")
    fun `parse correct pricingOptions object`(itineraryPosition: Int, pricingOptions: List<ApiResponsePricingOptions>) {
        val responseSearch = loadFromFile("response_json_pending.json")

        assertThat(
            responseSearch.itineraries?.get(itineraryPosition)?.pricingOptions
        ).isEqualTo(
            pricingOptions
        )
    }

    @Test
    @Parameters(method = "leg objects")
    fun `parse leg object correctly`(outboundLeg: ApiResponseLeg, isEqual: Boolean) {
        val responseSearch = loadFromFile("response_json_pending.json")

        assertThat(
            responseSearch.itineraries?.get(0)?.outBoundLeg == outboundLeg
        ).isEqualTo(
            isEqual
        )
    }

    private companion object {

        const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

        val gsonObject: Gson = GsonBuilder()
            .registerTypeAdapterFactory(SearchFlightsTypeAdapter())
            .setDateFormat(DATE_FORMAT)
            .create()

        fun loadFromFile(fileName: String): ApiResponseSearch {
            val inputStream = ClassLoader.getSystemResourceAsStream(fileName)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            return gsonObject.fromJson(jsonString, ApiResponseSearch::class.java)
        }

        @JvmStatic
        private fun `pricingOptions results`(): Array<Array<Any>> {
            val pricingOptionsAtFirstPosition = listOf(
                ApiResponsePricingOptions(
                    price = 347.82.toBigDecimal(),
                    agents = listOf(
                        ApiResponseAgents(
                            id = 2628363,
                            name = "GotoGate",
                            imageUrl = "https://s1.apideeplink.com/images/websites/gtuk.png",
                            type = "TravelAgent"
                        )
                    )
                )
            )
            val pricingOptionsAtSecondPosition = listOf(
                ApiResponsePricingOptions(
                    price = 293.58.toBigDecimal(),
                    agents = listOf(
                        ApiResponseAgents(
                            id = 2032127,
                            name = "British Airways",
                            imageUrl = "https://s1.apideeplink.com/images/websites/ba__.png",
                            type = "Airline"
                        ),
                        ApiResponseAgents(
                            id = 2628363,
                            name = "GotoGate",
                            imageUrl = "https://s1.apideeplink.com/images/websites/gtuk.png",
                            type = "TravelAgent"
                        )
                    )
                ),
                ApiResponsePricingOptions(
                    price = 332.58.toBigDecimal(),
                    agents = listOf(
                        ApiResponseAgents(
                            id = 4499211,
                            name = "Expedia",
                            imageUrl = "https://s1.apideeplink.com/images/websites/xpuk.png",
                            type = "TravelAgent"
                        )
                    )
                ),
                ApiResponsePricingOptions(
                    price = 335.58.toBigDecimal(),
                    agents = listOf(
                        ApiResponseAgents(
                            id = 2365707,
                            name = "ebookers",
                            imageUrl = "https://s1.apideeplink.com/images/websites/ebuk.png",
                            type = "TravelAgent"
                        )
                    )
                ),
                ApiResponsePricingOptions(
                    price = 395.toBigDecimal(),
                    agents = listOf(
                        ApiResponseAgents(
                            id = 3934928,
                            name = "Kiwi.com",
                            imageUrl = "https://s1.apideeplink.com/images/websites/skyp.png",
                            type = "TravelAgent"
                        )
                    )
                )
            )

            return arrayOf(
                arrayOf(0, pricingOptionsAtFirstPosition),
                arrayOf(1, pricingOptionsAtSecondPosition)
            )
        }

        @JvmStatic
        private fun `leg objects`(): Array<Array<Any>> {

            val dateFormat = SimpleDateFormat(DATE_FORMAT)

            val expectedOutboundLeg = ApiResponseLeg(
                id = "11235-1903291645--32753-1-13554-1903292205",
                originStation = ApiResponsePlaces(
                    id = 11235,
                    code = "EDI",
                    type = "Airport",
                    name = "Edinburgh"
                ),
                destinationStation = ApiResponsePlaces(
                    id = 13554,
                    code = "LHR",
                    type = "Airport",
                    name = "London Heathrow"
                ),
                departureTime = dateFormat.parse("2019-03-29T16:45:00"),
                arrivalTime = dateFormat.parse("2019-03-29T22:05:00"),
                carriers = listOf(
                    ApiResponseCarrier(
                        id = 1033,
                        code = "EI",
                        name = "Aer Lingus",
                        imageUrl = "https://s1.apideeplink.com/images/airlines/EI.png",
                        displayCode = "EI"
                    )
                ),
                operatingCarriers = listOf(
                    ApiResponseCarrier(
                        id = 1653,
                        code = "RE",
                        name = "Stobart Air",
                        imageUrl = "https://s1.apideeplink.com/images/airlines/RE.png",
                        displayCode = "RE"
                    ),
                    ApiResponseCarrier(
                        id = 1033,
                        code = "EI",
                        name = "Aer Lingus",
                        imageUrl = "https://s1.apideeplink.com/images/airlines/EI.png",
                        displayCode = "EI"
                    )
                ),
                duration = 320,
                journeymode = "Flight",
                directionality = "Outbound",
                stops = listOf(11154),
                segments = listOf(
                    ApiResponseSegment(
                        id = 87,
                        originStation = ApiResponsePlaces(
                            id = 11235,
                            code = "EDI",
                            type = "Airport",
                            name = "Edinburgh"
                        ),
                        destinationStation = ApiResponsePlaces(
                            id = 11154,
                            code = "DUB",
                            type = "Airport",
                            name = "Dublin"
                        ),
                        departureTime = dateFormat.parse("2019-03-29T16:45:00"),
                        arrivalTime = dateFormat.parse("2019-03-29T18:00:00"),
                        carrier = ApiResponseCarrier(
                            id = 1033,
                            code = "EI",
                            name = "Aer Lingus",
                            imageUrl = "https://s1.apideeplink.com/images/airlines/EI.png",
                            displayCode = "EI"
                        ),
                        operatingCarrier = ApiResponseCarrier(
                            id = 1653,
                            code = "RE",
                            name = "Stobart Air",
                            imageUrl = "https://s1.apideeplink.com/images/airlines/RE.png",
                            displayCode = "RE"
                        ),
                        duration = 75,
                        flightNumber = "3555",
                        journeymode = "Flight",
                        directionality = "Outbound"
                    ),
                    ApiResponseSegment(
                        id = 88,
                        originStation = ApiResponsePlaces(
                            id = 11154,
                            code = "DUB",
                            type = "Airport",
                            name = "Dublin"
                        ),
                        destinationStation = ApiResponsePlaces(
                            id = 13554,
                            code = "LHR",
                            type = "Airport",
                            name = "London Heathrow"
                        ),
                        departureTime = dateFormat.parse("2019-03-29T20:45:00"),
                        arrivalTime = dateFormat.parse("2019-03-29T22:05:00"),
                        carrier = ApiResponseCarrier(
                            id = 1033,
                            code = "EI",
                            name = "Aer Lingus",
                            imageUrl = "https://s1.apideeplink.com/images/airlines/EI.png",
                            displayCode = "EI"
                        ),
                        operatingCarrier = ApiResponseCarrier(
                            id = 1033,
                            code = "EI",
                            name = "Aer Lingus",
                            imageUrl = "https://s1.apideeplink.com/images/airlines/EI.png",
                            displayCode = "EI"
                        ),
                        duration = 80,
                        flightNumber = "186",
                        journeymode = "Flight",
                        directionality = "Outbound"
                    )
                )
            )

            val differentOutboundLeg = ApiResponseLeg(
                id = "11235-1903291645--32753-1-13554-1903292205",
                originStation = ApiResponsePlaces(
                    id = 11235,
                    code = "EDI",
                    type = "Airport",
                    name = "Edinburgh"
                ),
                destinationStation = ApiResponsePlaces(
                    id = 13554,
                    code = "LHR",
                    type = "Airport",
                    name = "London Heathrow"
                ),
                departureTime = dateFormat.parse("2019-03-29T16:45:00"),
                arrivalTime = dateFormat.parse("2019-03-29T22:05:00"),
                carriers = listOf(
                    ApiResponseCarrier(
                        id = 1033,
                        code = "EI",
                        name = "Aer Lingus",
                        imageUrl = "https://s1.apideeplink.com/images/airlines/EI.png",
                        displayCode = "EI"
                    )
                ),
                operatingCarriers = listOf(
                    ApiResponseCarrier(
                        id = 1653,
                        code = "RE",
                        name = "Stobart Air",
                        imageUrl = "https://s1.apideeplink.com/images/airlines/RE.png",
                        displayCode = "RE"
                    ),
                    ApiResponseCarrier(
                        id = 1033,
                        code = "EI",
                        name = "Aer Lingus",
                        imageUrl = "https://s1.apideeplink.com/images/airlines/EI.png",
                        displayCode = "EI"
                    )
                ),
                duration = 320,
                journeymode = "Flight",
                directionality = "Outbound",
                stops = listOf(11154),
                segments = listOf(
                    ApiResponseSegment(
                        id = 87,
                        originStation = ApiResponsePlaces(
                            id = 11235,
                            code = "EDI",
                            type = "Airport",
                            name = "Edinburgh"
                        ),
                        destinationStation = ApiResponsePlaces(
                            id = 11154,
                            code = "DUB",
                            type = "Airport",
                            name = "Dublin"
                        ),
                        departureTime = dateFormat.parse("2019-03-29T16:45:00"),
                        arrivalTime = dateFormat.parse("2019-03-29T18:00:00"),
                        carrier = ApiResponseCarrier(
                            id = 1033,
                            code = "EI",
                            name = "Aer Lingus",
                            imageUrl = "https://s1.apideeplink.com/images/airlines/EI.png",
                            displayCode = "EI"
                        ),
                        operatingCarrier = ApiResponseCarrier(
                            id = 1653,
                            code = "RE",
                            name = "Stobart Air",
                            imageUrl = "https://s1.apideeplink.com/images/airlines/RE.png",
                            displayCode = "RE"
                        ),
                        duration = 75,
                        flightNumber = "3555",
                        journeymode = "Flight",
                        directionality = "Outbound"
                    )
                )
            )

            return arrayOf(
                arrayOf(expectedOutboundLeg, true),
                arrayOf(differentOutboundLeg, false)
            )
        }
    }
}
