package com.haroldo.searchforflights.flightsresults.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haroldo.searchforflights.R
import com.haroldo.searchforflights.model.Itinerary
import kotlinx.android.synthetic.main.view_itinenary_item.view.*
import kotlinx.android.synthetic.main.view_leg.view.*
import javax.inject.Inject
import javax.inject.Provider

class FlightsResultAdapter @Inject constructor(
    private val itemPresenterProvider: Provider<ItinerariesItemPresenter>
) : RecyclerView.Adapter<FlightsResultViewHolder>() {

    private var items: List<Itinerary> = emptyList()
    private var onItemClick: (Itinerary) -> Unit = {}

    fun updateItems(
        items: List<Itinerary>,
        diffResult: DiffUtil.DiffResult,
        onItemClick: (Itinerary) -> Unit
    ) {
        diffResult.dispatchUpdatesTo(this)

        this.items = items
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightsResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_itinenary_item, parent)
        return FlightsResultViewHolder(view)
    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: FlightsResultViewHolder, position: Int) {
        with(items[position]) {
            holder.itemView.setOnClickListener { onItemClick(this) }
            holder.bind(this, itemPresenterProvider)
        }
    }

    /**
     * Renders only the changed parts of the calculated diff
     */
    override fun onBindViewHolder(holder: FlightsResultViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle = payloads[0] as Bundle

            if (bundle.containsKey(IS_CHEAPEST)) {
                holder.updateCheapest(bundle.getBoolean(IS_CHEAPEST))
            }

            if (bundle.containsKey(IS_SHORTEST)) {
                holder.updateShortest(bundle.getBoolean(IS_SHORTEST))
            }

            if (bundle.containsKey(RATING)) {
                holder.updateRating(bundle.getString(RATING)!!)
            }
        }
    }
}


class FlightsResultViewHolder(
    view: View
) : RecyclerView.ViewHolder(view), ItinerariesItemView {

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

    private lateinit var presenter: ItinerariesItemPresenter

    fun bind(itinerary: Itinerary, presenterProvider: Provider<ItinerariesItemPresenter>) {
        this.presenter = presenterProvider.get()
        presenter.onAttach(this, itinerary)
    }

    fun updateCheapest(cheapest: Boolean) {
        presenter.updateCheapest(cheapest)
    }

    fun updateShortest(shortest: Boolean) {
        presenter.updateShortest(shortest = shortest)
    }

    fun updateRating(rating: String) {
        presenter.updateRating(rating = rating)
    }

    override fun setLogoCarriers(logoCarrierOut: String, logoCarrierIn: String) {
        Glide
            .with(outboundCarrierLogo)
            .load(logoCarrierOut)
            .centerCrop()
            .into(outboundCarrierLogo)

        Glide
            .with(inboundCarrierLogo)
            .load(logoCarrierIn)
            .centerCrop()
            .into(inboundCarrierLogo)
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
        this.ratingImage.setImageResource(ratingImage)
    }

    override fun setAgent(agentStr: String) {
        agent.text = agentStr
    }

    override fun setPrice(priceStr: String) {
        price.text = priceStr
    }
}