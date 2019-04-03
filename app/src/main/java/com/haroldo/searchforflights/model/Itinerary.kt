package com.haroldo.searchforflights.model

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
) {
    override fun hashCode(): Int {
        return (outboundLeg.id + inboundLeg.id).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Itinerary

        if (price != other.price) return false
        if (agentNames != other.agentNames) return false
        if (outboundLeg != other.outboundLeg) return false
        if (inboundLeg != other.inboundLeg) return false

        return true
    }
}

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
