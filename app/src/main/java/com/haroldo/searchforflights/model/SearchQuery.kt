package com.haroldo.searchforflights.model

import android.os.Parcelable
import com.haroldo.searchforflights.model.api.ApiCreateSessionRequest
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchQuery(
    val cabinClass: String = "Economy",
    val country: String = "UK",
    val currency: String = "GBP",
    val locale: String = "en-GB",
    val originPlace: String = "EDI-sky",
    val destinationPlace: String = "LOND-sky",
    val outboundDate: String,
    val inboundDate: String,
    val adults: Int = 1,
    val children: Int = 0,
    val infants: Int = 0
) : Parcelable
