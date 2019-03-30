package com.challenge.searchforflights.flightsresults.model

import java.util.*

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
