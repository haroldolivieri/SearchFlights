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
): Float {

    val stopRating = stopsSum().inverseNormalization(
        max = stopsReference.max.toFloat(),
        min = stopsReference.min.toFloat()
    ) * 10

    val durationRating = durationSum().inverseNormalization(
        max = durationReference.max.toFloat(),
        min = durationReference.min.toFloat()
    ) * 10

    val priceRating = price.inverseNormalization(
        max = priceReference.max,
        min = priceReference.min
    ) * 10.toBigDecimal()

    return (stopRating + durationRating + priceRating.toFloat()) / 3
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

    val stopsReference = MinMax(minStops, maxStops)

    val minDuration = minBy { it.durationSum() }!!.durationSum()
    val maxDuration = maxBy { it.durationSum() }!!.durationSum()

    val durationReference = MinMax(minDuration, maxDuration)

    val minPrice = minBy { it.price }!!.price
    val maxPrice = minBy { it.price }!!.price

    val priceReference = MinMax(minPrice, maxPrice)

    return Triple(stopsReference, durationReference, priceReference)
}

private data class MinMax<T>(
    val min: T,
    val max: T
)
