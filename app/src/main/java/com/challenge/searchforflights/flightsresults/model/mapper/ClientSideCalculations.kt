package com.challenge.searchforflights.flightsresults.model.mapper

import com.challenge.searchforflights.flightsresults.model.Itinerary
import java.math.BigDecimal

fun List<Itinerary>.clientSideCalculations() =
    run {
        val (stopsReference,
            durationReference,
            priceReference) = getMeasureReferences()

        map {
            with(it) {
                rating = calculateRating(stopsReference, durationReference, priceReference)

                if (price == priceReference.min) {
                    cheapest = true
                }

                if (durationSum() == durationReference.min) {
                    shortest = true
                }
                it
            }
        }
    }

private fun Itinerary.calculateRating(
    stopsReference: MinMax<Int>,
    durationReference: MinMax<Int>,
    priceReference: MinMax<BigDecimal>
): String {

    val stopRating = stopsSum().inverseNormalization(
        max = stopsReference.max.toFloat(),
        min = stopsReference.min.toFloat()
    )

    val durationRating = durationSum().inverseNormalization(
        max = durationReference.max.toFloat(),
        min = durationReference.min.toFloat()
    )

    val priceRating = price.inverseNormalization(
        max = priceReference.max,
        min = priceReference.min
    )

    return "%.1f".format((stopRating + durationRating + priceRating.toFloat()) * 10 / 3)
}

private fun Int.inverseNormalization(max: Float, min: Float): Float {
    return (max.minus(this)) / (max.minus(min))
}

private fun BigDecimal.inverseNormalization(max: BigDecimal, min: BigDecimal): BigDecimal {
    return (max.minus(this)) / (max.minus(min))
}

private fun Itinerary.stopsSum() =
    outboundLeg.stopsAmount + inboundLeg.stopsAmount

private fun Itinerary.durationSum() =
    outboundLeg.duration + inboundLeg.duration

private fun List<Itinerary>.getMeasureReferences(): Triple<MinMax<Int>, MinMax<Int>, MinMax<BigDecimal>> {
    val minStops = minBy { it.stopsSum() }!!.stopsSum()
    val maxStops = maxBy { it.stopsSum() }!!.stopsSum()

    val stopsReference = MinMax(min = minStops, max = maxStops)

    val minDuration = minBy { it.durationSum() }!!.durationSum()
    val maxDuration = maxBy { it.durationSum() }!!.durationSum()

    val durationReference = MinMax(min = minDuration, max = maxDuration)

    val minPrice = minBy { it.price }!!.price
    val maxPrice = maxBy { it.price }!!.price

    val priceReference = MinMax(min = minPrice, max = maxPrice)

    return Triple(stopsReference, durationReference, priceReference)
}

private data class MinMax<T>(
    val min: T,
    val max: T
)
