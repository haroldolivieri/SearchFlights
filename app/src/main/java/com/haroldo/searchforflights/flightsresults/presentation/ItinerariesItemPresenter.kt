package com.haroldo.searchforflights.flightsresults.presentation

import androidx.annotation.DrawableRes
import com.haroldo.searchforflights.R
import com.haroldo.searchforflights.model.Itinerary
import com.haroldo.searchforflights.model.Leg
import org.joda.time.LocalTime
import javax.inject.Inject


class ItinerariesItemPresenter @Inject constructor() {

    private var view: ItinerariesItemView? = null

    fun onAttach(view: ItinerariesItemView, itinerary: Itinerary) {
        this.view = view
        with(itinerary) {
            view.run {
                setLogoCarriers(outboundLeg.carrier.imageUrl, inboundLeg.carrier.imageUrl)
                setDepartureArrivalTime(
                    getDepartureArrivalTimeByLeg(outboundLeg),
                    getDepartureArrivalTimeByLeg(inboundLeg)
                )
                setPlacesAndAgent(getPlacesByLeg(outboundLeg), getPlacesByLeg(inboundLeg))
                setType(getTypeByLeg(outboundLeg), getTypeByLeg(inboundLeg))
                setDuration(getDurationInHours(outboundLeg), getDurationInHours(inboundLeg))
                setRating(rating, getRatingResource(rating))
                setPrice("£ $price")

                if (cheapest) {

                }

                if (shortest) {

                }
            }
        }
    }

    private fun getTypeByLeg(leg: Leg): String =
        if (leg.stopsAmount == 0) {
            "Direct"
        } else {
            "${leg.stopsAmount} Stops"
        }

    private fun getPlacesByLeg(leg: Leg): String {
        return "${leg.origin.code} - ${leg.destination.code}"
    }

    private fun getDepartureArrivalTimeByLeg(leg: Leg): String {
        val departureHour = LocalTime(leg.departureTime).hourOfDay().asText
        val arrivalHour = LocalTime(leg.arrivalTime).hourOfDay().asText
        return "$departureHour - $arrivalHour"
    }

    private fun getRatingResource(rating: String) =
        when (rating.toFloat()) {
            in 0.0..1.9 -> R.drawable.ic_sentiment_very_dissatisfied_24dp
            in 2.0..3.9 -> R.drawable.ic_sentiment_dissatisfied_24dp
            in 4.0..5.9 -> R.drawable.ic_sentiment_neutral_24dp
            in 6.0..8.9 -> R.drawable.ic_sentiment_satisfied_24dp
            in 9.0..10.0 -> R.drawable.ic_sentiment_very_satisfied_24dp
            else -> -1
        }

    private fun getDurationInHours(leg: Leg): String {
        val duration = leg.duration.toLong()
        val hours = leg.duration.toLong() / 60
        val minutesRemaining = duration % 60

        return "${hours}h ${minutesRemaining}m"
    }

    fun updateCheapest(cheapest: Boolean) {

    }

    fun updateShortest(shortest: Boolean) {
    }

    fun updateRating(rating: String) {
        view?.setRating(rating, getRatingResource(rating))
    }
}

interface ItinerariesItemView {
    fun setLogoCarriers(logoCarrierOut: String, logoCarrierIn: String)
    fun setDepartureArrivalTime(departureArrivalTimeOut: String, departureArrivalTimeIn: String)
    fun setPlacesAndAgent(placesOut: String, placesIn: String)
    fun setType(typeOut: String, typeIn: String)
    fun setDuration(durationOut: String, durationIn: String)
    fun setRating(rating: String, @DrawableRes ratingImage: Int)

    fun setAgent(agentStr: String)
    fun setPrice(priceStr: String)
}