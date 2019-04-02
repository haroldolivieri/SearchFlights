package com.haroldo.searchforflights.model.api

import com.google.gson.annotations.SerializedName

data class ApiCreateSessionRequest(
    @SerializedName("cabinclass") val cabinClass: String,
    @SerializedName("country") val country: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("locale") val locale: String,
    @SerializedName("locationSchema") val locationSchema: String,
    @SerializedName("originplace") val originPlace: String,
    @SerializedName("destinationplace") val destinationPlace: String,
    @SerializedName("outbounddate") val outboundDate: String,
    @SerializedName("inbounddate") val inboundDate: String,
    @SerializedName("adults") val adults: Int,
    @SerializedName("children") val children: Int,
    @SerializedName("infants") val infants: Int
)