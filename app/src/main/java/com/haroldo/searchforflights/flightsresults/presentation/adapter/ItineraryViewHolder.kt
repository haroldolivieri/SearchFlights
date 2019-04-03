package com.haroldo.searchforflights.flightsresults.presentation.adapter

import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haroldo.searchforflights.changeVisibility
import com.haroldo.searchforflights.model.Itinerary
import kotlinx.android.synthetic.main.item_itinenary.view.*
import kotlinx.android.synthetic.main.view_leg.view.*
import javax.inject.Provider


class ItineraryViewHolder(
    view: View
) : RecyclerView.ViewHolder(view),
    ItinerariesItemView {

    private val outboundCarrierLogo = view.outboundLeg.carrierLogo
    private val outboundDepartureArrivalTime = view.outboundLeg.departureAndArrivalTime
    private val outboundPlacesAndAgent = view.outboundLeg.placesAndAgent
    private val outboundType = view.outboundLeg.type
    private val outboundDuration = view.outboundLeg.totalDuration

    private val inboundCarrierLogo = view.inboundLeg.carrierLogo
    private val inboundDepartureArrivalTime = view.inboundLeg.departureAndArrivalTime
    private val inboundPlacesAndAgent = view.inboundLeg.placesAndAgent
    private val inboundType = view.inboundLeg.type
    private val inboundDuration = view.inboundLeg.totalDuration

    private val ratingText = view.rating
    private val ratingImage = view.ratingImage
    private val price = view.price
    private val agent = view.agent

    private val cheapest = view.chepeast
    private val shortest = view.shortest

    private lateinit var presenter: ItinerariesItemPresenter

    fun bind(itinerary: Itinerary, presenterProvider: Provider<ItinerariesItemPresenter>) {
        this.presenter = presenterProvider.get()
        presenter.onAttach(this, itinerary)
    }

    fun updateCheapest(cheapest: Boolean) {
        presenter.setupCheapest(cheapest)
    }

    fun updateShortest(shortest: Boolean) {
        presenter.setupShortest(shortest)
    }

    fun updateRating(rating: String) {
        presenter.updateRating(rating)
    }

    override fun setLogoCarriers(logoCarrierOut: String, logoCarrierIn: String) {
        Glide
            .with(outboundCarrierLogo)
            .load(logoCarrierOut)
            .into(outboundCarrierLogo)

        Glide
            .with(inboundCarrierLogo)
            .load(logoCarrierIn)
            .into(inboundCarrierLogo)
    }

    override fun setCheapestVisibility(visible: Boolean) {
        cheapest.changeVisibility(visible)
    }

    override fun changeShortestVisibility(visible: Boolean) {
        shortest.changeVisibility(visible)
    }

    override fun setDepartureArrivalTime(departureArrivalTimeOut: String, departureArrivalTimeIn: String) {
        outboundDepartureArrivalTime.text = departureArrivalTimeOut
        inboundDepartureArrivalTime.text = departureArrivalTimeIn
    }

    override fun setPlacesAndAgent(placesOut: String, placesIn: String) {
        outboundPlacesAndAgent.text = placesOut
        inboundPlacesAndAgent.text = placesIn
    }

    override fun setType(typeOut: String, typeIn: String) {
        outboundType.text = typeOut
        inboundType.text = typeIn
    }

    override fun setDuration(durationOut: String, durationIn: String) {
        outboundDuration.text = durationOut
        inboundDuration.text = durationIn
    }

    override fun setRating(rating: String, @DrawableRes ratingImage: Int) {
        ratingText.text = rating
        if (ratingImage != -1) {
            this.ratingImage.setImageResource(ratingImage)
        }
    }

    override fun setAgent(agentStr: String) {
        agent.text = agentStr
    }

    override fun setPrice(priceStr: String) {
        price.text = priceStr
    }
}