package com.haroldo.searchforflights.model.api

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.*

data class ApiResponseSearch(
    @SerializedName("parsedItineraries") val itineraries: List<ApiResponseItinerary>,
    @SerializedName("Status") val status: Status
)

data class ApiResponseItinerary(
    @SerializedName("outboundLeg") val outBoundLeg: ApiResponseLeg,
    @SerializedName("inboundLeg") val inboundLeg: ApiResponseLeg,
    @SerializedName("pricingOptions") val pricingOptions: List<ApiResponsePricingOption>
)

data class ApiResponsePricingOption(
    @SerializedName("agents") val agents: List<ApiResponseAgent>,
    @SerializedName("Price") val price: BigDecimal
)

data class ApiResponseAgent(
    @SerializedName("Id") val id: Int,
    @SerializedName("Name") val name: String,
    @SerializedName("ImageUrl") val imageUrl: String,
    @SerializedName("Type") val type: String
)

data class ApiResponseLeg(
    @SerializedName("Id") val id: String,
    @SerializedName("originStation") val originStation: ApiResponsePlace,
    @SerializedName("destinationStation") val destinationStation: ApiResponsePlace,
    @SerializedName("Departure") val departureTime: Date,
    @SerializedName("Arrival") val arrivalTime: Date,
    @SerializedName("carriers") val carriers: List<ApiResponseCarrier>,
    @SerializedName("operatingCarriers") val operatingCarriers: List<ApiResponseCarrier>,
    @SerializedName("Duration") val duration: Int,
    @SerializedName("JourneyMode") val journeymode: String,
    @SerializedName("Directionality") val directionality: String,
    @SerializedName("segments") val segments: List<ApiResponseSegment>,
    @SerializedName("Stops") val stops: List<Int>
)

data class ApiResponseSegment(
    @SerializedName("Id") val id: Int,
    @SerializedName("originStation") val originStation: ApiResponsePlace,
    @SerializedName("destinationStation") val destinationStation: ApiResponsePlace,
    @SerializedName("DepartureDateTime") val departureTime: Date,
    @SerializedName("ArrivalDateTime") val arrivalTime: Date,
    @SerializedName("carrier") val carrier: ApiResponseCarrier,
    @SerializedName("operatingCarrier") val operatingCarrier: ApiResponseCarrier,
    @SerializedName("Duration") val duration: Int,
    @SerializedName("FlightNumber") val flightNumber: String,
    @SerializedName("JourneyMode") val journeymode: String,
    @SerializedName("Directionality") val directionality: String
)

data class ApiResponseCarrier(
    @SerializedName("Id") val id: Int,
    @SerializedName("Code") val code: String,
    @SerializedName("Name") val name: String,
    @SerializedName("ImageUrl") val imageUrl: String,
    @SerializedName("DisplayCode") val displayCode: String
)

data class ApiResponsePlace(
    @SerializedName("Id") val id: Int,
    @SerializedName("Code") val code: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Name") val name: String
)

enum class Status {
    UpdatesComplete, UpdatesPending
}