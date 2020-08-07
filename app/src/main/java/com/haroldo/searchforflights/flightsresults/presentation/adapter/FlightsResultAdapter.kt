package com.haroldo.searchforflights.flightsresults.presentation.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.haroldo.searchforflights.R
import com.haroldo.searchforflights.model.Itinerary
import javax.inject.Inject
import javax.inject.Provider

private const val VIEW_TYPE_LOADING = 0
private const val VIEW_TYPE_NORMAL = 1
const val IS_CHEAPEST = "isCheapest"
const val IS_SHORTEST = "isShortest"
const val RATING = "rating"

class FlightsResultAdapter @Inject constructor(
    private val itemPresenterProvider: Provider<ItinerariesItemPresenter>
) : ListAdapter<Itinerary, RecyclerView.ViewHolder>(diffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_footer, parent, false)
                object : RecyclerView.ViewHolder(view) {}
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_itinenary, parent, false)
                ItineraryViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItineraryViewHolder) {
            holder.bind(getItem(position), itemPresenterProvider)
        }
    }

    override fun getItemViewType(position: Int) =
        if (getItem(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (holder is ItineraryViewHolder) {
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

    companion object {
        val diffUtilCallback = object : DiffUtil.ItemCallback<Itinerary>() {
            override fun areItemsTheSame(oldItem: Itinerary, newItem: Itinerary) =
                oldItem.inboundLeg.id == newItem.inboundLeg.id &&
                    oldItem.outboundLeg.id == newItem.outboundLeg.id

            override fun areContentsTheSame(oldItem: Itinerary, newItem: Itinerary) =
                oldItem == newItem

            override fun getChangePayload(oldItem: Itinerary, newItem: Itinerary): Bundle? {
                val bundle = Bundle()

                with(bundle) {
                    if (newItem.cheapest != oldItem.cheapest) {
                        putBoolean(IS_CHEAPEST, newItem.cheapest)
                    }

                    if (newItem.shortest != oldItem.shortest) {
                        putBoolean(IS_SHORTEST, newItem.shortest)
                    }

                    if (newItem.rating != oldItem.rating) {
                        putString(RATING, newItem.rating)
                    }
                }

                return if (bundle.size() > 0) bundle else null
            }
        }
    }
}