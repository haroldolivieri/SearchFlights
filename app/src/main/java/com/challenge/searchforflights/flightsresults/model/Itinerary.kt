package com.challenge.searchforflights.flightsresults.model

import java.math.BigDecimal

data class Itinerary(
    val price : BigDecimal,
    val agentNames: List<String>,
    val outboundLeg: Leg,
    val inboundLeg: Leg,
    var rating: Float = 0.0.toFloat(),
    var cheapest: Boolean = false,
    var shortest: Boolean = false
)
