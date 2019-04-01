package com.haroldo.searchforflights.model.mapper

import com.haroldo.searchforflights.model.*
import com.haroldo.searchforflights.model.api.*
import com.google.common.truth.Truth.assertThat
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat

@RunWith(JUnitParamsRunner::class)
class ItineraryResponseMapperTestShould {

    private val itineraryResponseMapper = ItineraryResponseMapper()

    @Test
    @Parameters(method = "itineraries list")
    fun `map response to model correctly`(
        responses: List<ApiResponseItinerary>,
        itineraries: List<Itinerary>,
        isEqual: Boolean
    ) {
        assertThat(itineraryResponseMapper.map(responses) == itineraries).isEqualTo(isEqual)
    }

    private companion object {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        val PRICING_OPTION_RESPONSE_0 = ApiResponsePricingOption(
            price = 293.58.toBigDecimal(),
            agents = listOf(
                ApiResponseAgent(
                    id = 2032127,
                    name = "British Airways",
                    imageUrl = "https://s1.apideeplink.com/images/websites/ba__.png",
                    type = "Airline"
                ),
                ApiResponseAgent(
                    id = 2628363,
                    name = "GotoGate",
                    imageUrl = "https://s1.apideeplink.com/images/websites/gtuk.png",
                    type = "TravelAgent"
                )
            )
        )

        val PRICING_OPTION_RESPONSE_1 = ApiResponsePricingOption(
            price = 332.58.toBigDecimal(),
            agents = listOf(
                ApiResponseAgent(
                    id = 4499211,
                    name = "Expedia",
                    imageUrl = "https://s1.apideeplink.com/images/websites/xpuk.png",
                    type = "TravelAgent"
                )
            )
        )

        val EDI_PLACE_RESPONSE = ApiResponsePlace(
            id = 11235,
            code = "EDI",
            type = "Airport",
            name = "Edinburgh"
        )

        val LHR_PLACE_RESPONSE = ApiResponsePlace(
            id = 13554,
            code = "LHR",
            type = "Airport",
            name = "London Heathrow"
        )

        val DUB_PLACE_RESPONSE = ApiResponsePlace(
            id = 12344,
            code = "DUB",
            type = "City",
            name = "Dublin"
        )

        val RE_CARRIER_RESPONSE = ApiResponseCarrier(
            id = 1653,
            code = "RE",
            name = "Stobart Air",
            imageUrl = "https://s1.apideeplink.com/images/airlines/RE.png",
            displayCode = "RE"
        )

        val EI_CARRIER_RESPONSE = ApiResponseCarrier(
            id = 1033,
            code = "EI",
            name = "Aer Lingus",
            imageUrl = "https://s1.apideeplink.com/images/airlines/EI.png",
            displayCode = "EI"
        )

        val SEGMENT_RESPONSE_0 =
            ApiResponseSegment(
                id = 87,
                originStation = EDI_PLACE_RESPONSE,
                destinationStation = LHR_PLACE_RESPONSE,
                departureTime = dateFormat.parse("2019-03-29T16:45:00"),
                arrivalTime = dateFormat.parse("2019-03-29T18:00:00"),
                carrier = RE_CARRIER_RESPONSE,
                operatingCarrier = RE_CARRIER_RESPONSE,
                duration = 75,
                flightNumber = "3555",
                journeymode = "Flight",
                directionality = "Outbound"
            )

        val SEGMENT_RESPONSE_1 =
            ApiResponseSegment(
                id = 88,
                originStation = DUB_PLACE_RESPONSE,
                destinationStation = LHR_PLACE_RESPONSE,
                departureTime = dateFormat.parse("2019-03-29T16:45:00"),
                arrivalTime = dateFormat.parse("2019-03-29T18:00:00"),
                carrier = RE_CARRIER_RESPONSE,
                operatingCarrier = EI_CARRIER_RESPONSE,
                duration = 75,
                flightNumber = "3555",
                journeymode = "Flight",
                directionality = "Outbound"
            )

        val EDI_PLACE = Place(
            id = 11235,
            code = "EDI",
            type = "Airport",
            name = "Edinburgh"
        )

        val LHR_PLACE = Place(
            id = 13554,
            code = "LHR",
            type = "Airport",
            name = "London Heathrow"
        )

        val DUB_PLACE = Place(
            id = 12344,
            code = "DUB",
            type = "City",
            name = "Dublin"
        )

        val RE_CARRIER = Carrier(
            id = 1653,
            code = "RE",
            name = "Stobart Air",
            imageUrl = "https://s1.apideeplink.com/images/airlines/RE.png",
            displayCode = "RE"
        )

        val EI_CARRIER = Carrier(
            id = 1033,
            code = "EI",
            name = "Aer Lingus",
            imageUrl = "https://s1.apideeplink.com/images/airlines/EI.png",
            displayCode = "EI"
        )

        val SEGMENT_0 =
            Segment(
                id = 87,
                origin = EDI_PLACE,
                destination = LHR_PLACE,
                departureTime = dateFormat.parse("2019-03-29T16:45:00"),
                arrivalTime = dateFormat.parse("2019-03-29T18:00:00"),
                carrier = RE_CARRIER,
                duration = 75,
                flightNumber = "3555"
            )

        val SEGMENT_1 =
            Segment(
                id = 88,
                origin = DUB_PLACE,
                destination = LHR_PLACE,
                departureTime = dateFormat.parse("2019-03-29T16:45:00"),
                arrivalTime = dateFormat.parse("2019-03-29T18:00:00"),
                carrier = RE_CARRIER,
                duration = 75,
                flightNumber = "3555"
            )

        val API_RESPONSE_ITINERARY_0 = ApiResponseItinerary(
            outBoundLeg = givenApiLeg(
                id = "outleg_0",
                origin = EDI_PLACE_RESPONSE,
                destination = LHR_PLACE_RESPONSE,
                carrier = RE_CARRIER_RESPONSE,
                operatingCarrier = EI_CARRIER_RESPONSE,
                segments = listOf(SEGMENT_RESPONSE_0, SEGMENT_RESPONSE_1),
                stops = listOf(1, 2, 3, 4)
            ),
            inboundLeg = givenApiLeg(
                id = "inleg_0",
                origin = LHR_PLACE_RESPONSE,
                destination = EDI_PLACE_RESPONSE,
                carrier = RE_CARRIER_RESPONSE,
                operatingCarrier = EI_CARRIER_RESPONSE,
                segments = listOf(SEGMENT_RESPONSE_1, SEGMENT_RESPONSE_0),
                stops = listOf(1, 2)
            ),
            pricingOptions = listOf(PRICING_OPTION_RESPONSE_0, PRICING_OPTION_RESPONSE_1)
        )

        val EXPECTED_ITINERARY_OBJECT_0 = Itinerary(
            price = 293.58.toBigDecimal(),
            agentNames = listOf("British Airways", "GotoGate"),
            outboundLeg = givenLeg(
                id = "outleg_0",
                origin = EDI_PLACE,
                destination = LHR_PLACE,
                carrier = RE_CARRIER,
                segments = listOf(SEGMENT_0, SEGMENT_1),
                stopsAmount = 4
                ),
            inboundLeg = givenLeg(
                id = "inleg_0",
                origin = LHR_PLACE,
                destination = EDI_PLACE,
                carrier = RE_CARRIER,
                segments = listOf(SEGMENT_1, SEGMENT_0),
                stopsAmount = 2
            )
        )

        val API_RESPONSE_ITINERARY_1 = ApiResponseItinerary(
            outBoundLeg = givenApiLeg(
                id = "outleg_1",
                origin = DUB_PLACE_RESPONSE,
                destination = EDI_PLACE_RESPONSE,
                carrier = EI_CARRIER_RESPONSE,
                operatingCarrier = EI_CARRIER_RESPONSE,
                segments = listOf(SEGMENT_RESPONSE_0, SEGMENT_RESPONSE_1),
                stops = listOf(1, 2)
            ),
            inboundLeg = givenApiLeg(
                id = "inleg_1",
                origin = EDI_PLACE_RESPONSE,
                destination = DUB_PLACE_RESPONSE,
                carrier = RE_CARRIER_RESPONSE,
                operatingCarrier = EI_CARRIER_RESPONSE,
                segments = listOf(SEGMENT_RESPONSE_0, SEGMENT_RESPONSE_1),
                stops = emptyList()
            ),
            pricingOptions = listOf(PRICING_OPTION_RESPONSE_1)
        )

        val EXPECTED_ITINERARY_OBJECT_1 = Itinerary(
            price = 332.58.toBigDecimal(),
            agentNames = listOf("Expedia"),
            outboundLeg = givenLeg(
                id = "outleg_1",
                origin = DUB_PLACE,
                destination = EDI_PLACE,
                carrier = EI_CARRIER,
                segments = listOf(SEGMENT_0, SEGMENT_1),
                stopsAmount = 2
            ),
            inboundLeg = givenLeg(
                id = "inleg_1",
                origin = EDI_PLACE,
                destination = DUB_PLACE,
                carrier = RE_CARRIER,
                segments = listOf(SEGMENT_0, SEGMENT_1),
                stopsAmount = 0
            )
        )

        @JvmStatic
        fun `itineraries list`() = arrayOf(
            arrayOf(
                listOf(API_RESPONSE_ITINERARY_0, API_RESPONSE_ITINERARY_1),
                listOf(EXPECTED_ITINERARY_OBJECT_0, EXPECTED_ITINERARY_OBJECT_1),
                true
            ),
            arrayOf(
                listOf(API_RESPONSE_ITINERARY_0, API_RESPONSE_ITINERARY_0),
                listOf(EXPECTED_ITINERARY_OBJECT_0, EXPECTED_ITINERARY_OBJECT_1),
                false
            )
        )

        fun givenLeg(
            id: String,
            origin: Place,
            destination: Place,
            carrier: Carrier,
            segments: List<Segment>,
            stopsAmount: Int
        ) = Leg(
            id = id,
            origin = origin,
            destination = destination,
            departureTime = dateFormat.parse("2019-03-29T16:45:00"),
            arrivalTime = dateFormat.parse("2019-03-29T22:05:00"),
            carrier = carrier,
            duration = 320,
            segments = segments,
            stopsAmount = stopsAmount
        )

        fun givenApiLeg(
            id: String,
            origin: ApiResponsePlace,
            destination: ApiResponsePlace,
            carrier: ApiResponseCarrier,
            operatingCarrier: ApiResponseCarrier,
            segments: List<ApiResponseSegment>,
            stops: List<Int>
        ) =
            ApiResponseLeg(
                id = id,
                originStation = origin,
                destinationStation = destination,
                departureTime = dateFormat.parse("2019-03-29T16:45:00"),
                arrivalTime = dateFormat.parse("2019-03-29T22:05:00"),
                carriers = listOf(carrier),
                operatingCarriers = listOf(operatingCarrier),
                duration = 320,
                journeymode = "Flight",
                directionality = "Outbound",
                stops = stops,
                segments = segments
            )

    }
}