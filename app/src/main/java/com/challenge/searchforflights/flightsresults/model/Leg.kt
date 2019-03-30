package com.challenge.searchforflights.flightsresults.model

import java.util.*

data class Leg (
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
