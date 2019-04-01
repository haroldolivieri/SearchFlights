package com.haroldo.searchforflights.model.mapper

import com.haroldo.searchforflights.di.FlightsResultScope
import com.haroldo.searchforflights.model.*
import com.haroldo.searchforflights.model.api.*
import javax.inject.Inject
import javax.inject.Singleton

@FlightsResultScope
class ItineraryResponseMapper @Inject constructor() {

    fun map(apiResponseItineraries: List<ApiResponseItinerary>) =
        apiResponseItineraries.map { apiResponseItinerary ->
            with(apiResponseItinerary) {

                val lowerPricingOption = pricingOptions.getLowerPrice()

                Itinerary(
                    price = lowerPricingOption.price,
                    agentNames = lowerPricingOption.agents.map { it.name },
                    outboundLeg = outBoundLeg.toLeg(),
                    inboundLeg = inboundLeg.toLeg()
                )
            }
        }

    private fun ApiResponsePlace.toPlace() =
        Place(
            id = id,
            code = code,
            type = type,
            name = name
        )

    private fun ApiResponseCarrier.toCarrier() = Carrier(
        id = id,
        name = name,
        code = code,
        imageUrl = imageUrl,
        displayCode = displayCode
    )

    private fun ApiResponseLeg.toLeg() =
        Leg(
            id = id,
            origin = originStation.toPlace(),
            destination = destinationStation.toPlace(),
            departureTime = departureTime,
            arrivalTime = arrivalTime,
            carrier = carriers.first().toCarrier(),
            duration = duration,
            segments = segments.toSegments(),
            stopsAmount = stops.size
        )

    private fun List<ApiResponseSegment>.toSegments() =
        map { response ->
            with(response) {
                Segment(
                    id = id,
                    origin = originStation.toPlace(),
                    destination = destinationStation.toPlace(),
                    departureTime = departureTime,
                    arrivalTime = arrivalTime,
                    carrier = carrier.toCarrier(),
                    duration = duration,
                    flightNumber = flightNumber
                )
            }
        }

    private fun List<ApiResponsePricingOption>.getLowerPrice() = minBy { it.price }!!
}