package com.haroldo.searchforflights.flightsresults.presentation

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.haroldo.searchforflights.model.Itinerary


const val IS_CHEAPEST = "isCheapest"
const val IS_SHORTEST = "isShortest"
const val RATING = "rating"

class ItinerariesDiffCallback(
    private val newItineraries: List<Itinerary>,
    private val oldItineraries: List<Itinerary>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItineraries.size

    override fun getNewListSize() = newItineraries.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldItineraries[oldItemPosition] == newItineraries[newItemPosition]

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItineraryInboundLegId = oldItineraries[oldItemPosition].inboundLeg.id
        val oldItineraryOutboundLegId = oldItineraries[oldItemPosition].outboundLeg.id

        val newItineraryInboundLegId = newItineraries[newItemPosition].inboundLeg.id
        val newItineraryOutboundLegId = newItineraries[newItemPosition].outboundLeg.id

        return oldItineraryInboundLegId == newItineraryInboundLegId &&
                oldItineraryOutboundLegId == newItineraryOutboundLegId
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val newItinerary = newItineraries[newItemPosition]
        val oldItinerary = oldItineraries[oldItemPosition]

        val bundle = Bundle()

        when {
            newItinerary.cheapest != oldItinerary.cheapest ->
                bundle.putBoolean(IS_CHEAPEST, newItinerary.cheapest)
            newItinerary.shortest != oldItinerary.shortest ->
                bundle.putBoolean(IS_SHORTEST, newItinerary.shortest)
            newItinerary.rating != oldItinerary.rating ->
                bundle.putString(RATING, newItinerary.rating)
        }

        return if (bundle.size() > 0) bundle else null
    }
}