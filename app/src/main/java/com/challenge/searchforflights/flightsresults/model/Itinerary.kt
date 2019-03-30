package com.challenge.searchforflights.flightsresults.model

import java.math.BigDecimal

data class Itinerary(
    val price : BigDecimal,
    val agentNames: List<String>,
    val outboundLeg: Leg,
    val inboundLeg: Leg,
    var rating: String = "",
    var cheapest: Boolean = false,
    var shortest: Boolean = false
)