package com.haroldo.searchforflights.flightsresults.presentation.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.haroldo.searchforflights.R
import com.haroldo.searchforflights.flightsresults.presentation.IS_CHEAPEST
import com.haroldo.searchforflights.flightsresults.presentation.IS_SHORTEST
import com.haroldo.searchforflights.flightsresults.presentation.ItinerariesDiffCallback
import com.haroldo.searchforflights.flightsresults.presentation.RATING
import com.haroldo.searchforflights.model.Itinerary
import javax.inject.Inject
import javax.inject.Provider

private const val VIEW_TYPE_LOADING = 0
private const val VIEW_TYPE_NORMAL = 1

class FlightsResultAdapter @Inject constructor(
    private val itemPresenterProvider: Provider<ItinerariesItemPresenter>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<Itinerary?> = mutableListOf()

    fun updateItems(
        newItems: List<Itinerary>,
        isLastPageLoaded: Boolean
    ) {
        removeLoadingIfExists()

        this.items = newItems.toMutableList()

        if (!isLastPageLoaded) {
            addLoading()
        }

//        notifyDataSetChanged()
        calculateDiff(newItems)
    }

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

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItineraryViewHolder) {
            holder.bind(items[position]!!, itemPresenterProvider)
        }
    }

    override fun getItemViewType(position: Int) =
        if (items[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL


    private fun addLoading() {
        items.add(null)
    }

    private fun removeLoadingIfExists() {
        if (!items.isEmpty() && items[items.lastIndex] == null) {
            items.removeAt(items.lastIndex)
        }
    }

    /**
     * Renders only the changed parts of the calculated diffs
     */
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

    private fun calculateDiff(newItems: List<Itinerary>) {
        val diffCallback = ItinerariesDiffCallback(
            oldItineraries = items.toList(),
            newItineraries = newItems
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback, true)
        diffResult.dispatchUpdatesTo(this)
    }
}