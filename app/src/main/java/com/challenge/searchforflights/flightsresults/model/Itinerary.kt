package com.challenge.searchforflights.flightsresults.model

import java.math.BigDecimal
import java.util.*

data class Itinerary(
    val price: BigDecimal,
    val agentNames: List<String>,
    val outboundLeg: Leg,
    val inboundLeg: Leg,
    var rating: String = "",
    var cheapest: Boolean = false,
    var shortest: Boolean = false
)

data class Leg(
    val id: String,
    val origin: Place,
    val destination: Place,
    val departureTime: Date,
    val arrivalTime: Date,
    val carrier: Carrier,
    val duration: Int,
    val segments: List<Segment>,
    val stopsAmount: Int
)

data class Segment(
    val id: Int,
    val origin: Place,
    val destination: Place,
    val departureTime: Date,
    val arrivalTime: Date,
    val carrier: Carrier,
    val duration: Int,
    val flightNumber: String
)

data class Carrier(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val displayCode: String,
    val code: String
)

data class Place(
    val id: Int,
    val code: String,
    val type: String,
    val name: String
)

data class Agent(
    val id: Int,
    val name: String,
    val imageUrl: String
)
