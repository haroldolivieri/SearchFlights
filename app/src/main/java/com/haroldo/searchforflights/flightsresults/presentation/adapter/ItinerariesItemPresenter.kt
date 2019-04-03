package com.haroldo.searchforflights.flightsresults.presentation.adapter

import androidx.annotation.DrawableRes
import com.haroldo.searchforflights.R
import com.haroldo.searchforflights.model.Itinerary
import com.haroldo.searchforflights.model.Leg
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
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

                setupCheapest(cheapest)
                setupShortest(shortest)
                setAgent("Via ${agentNames[0]}")
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
        val formatter = DateTimeFormat.forPattern("HH:mm")

        val departure = DateTime(leg.departureTime)
        val arrival = DateTime(leg.arrivalTime)

        return "${formatter.print(departure)} - ${formatter.print(arrival)}"
    }

    private fun getRatingResource(rating: String) =
        when (rating.toFloat()) {
            in 0.0..2.0 -> R.drawable.ic_sentiment_very_dissatisfied_24dp
            in 2.0..4.0 -> R.drawable.ic_sentiment_dissatisfied_24dp
            in 4.0..6.0 -> R.drawable.ic_sentiment_neutral_24dp
            in 6.0..9.0 -> R.drawable.ic_sentiment_satisfied_24dp
            in 9.0..10.0 -> R.drawable.ic_sentiment_very_satisfied_24dp
            else -> -1
        }

    private fun getDurationInHours(leg: Leg): String {
        val duration = leg.duration.toLong()
        val hours = leg.duration.toLong() / 60
        val minutesRemaining = duration % 60

        return "${hours}h ${minutesRemaining}m"
    }

    fun setupCheapest(cheapest: Boolean) {
        view?.run {
            if (cheapest) {
                setCheapestVisibility(true)
            } else {
                setCheapestVisibility(false)
            }
        }
    }

    fun setupShortest(shortest: Boolean) {
        view?.run {
            if (shortest) {
                changeShortestVisibility(true)
            } else {
                changeShortestVisibility(false)
            }
        }
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
    fun setCheapestVisibility(visible: Boolean)
    fun changeShortestVisibility(visible: Boolean)
    fun setAgent(agentStr: String)
    fun setPrice(priceStr: String)
}