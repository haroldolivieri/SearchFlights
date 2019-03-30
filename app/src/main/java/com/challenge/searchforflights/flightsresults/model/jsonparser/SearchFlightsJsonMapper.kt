package com.challenge.searchforflights.flightsresults.model.jsonparser

import com.google.gson.JsonArray
import com.google.gson.JsonObject

class SearchFlightsJsonMapper constructor(parentJsonObject: JsonObject) {

    private val legsReference: JsonArray = parentJsonObject.getAsJsonArray("Legs")
    private val segmentsReference: JsonArray = parentJsonObject.getAsJsonArray("Segments")
    private val carriersReference: JsonArray = parentJsonObject.getAsJsonArray("Carriers")
    private val agentsReference: JsonArray = parentJsonObject.getAsJsonArray("Agents")
    private val placesReference: JsonArray = parentJsonObject.getAsJsonArray("Places")

    fun setupPricingOptions(itineraryObject: JsonObject) {
        itineraryObject.getAsJsonArray("PricingOptions").deepCopy().let {
            addElementsToPricingOptions(it)

            itineraryObject
                .addJsonElementProperty("pricingOptions", it)
                .removeProperty("PricingOptions")
        }
    }

    fun setupLeg(itineraryObject: JsonObject, legType: LegType) {
        val pair = if (legType == LegType.OUTBOUND) {
            Pair("InboundLegId", "inboundLeg")
        } else {
            Pair("OutboundLegId", "outboundLeg")
        }

        legsReference.getCopyOfObjectById(itineraryObject.get(pair.first).asString)?.let {
            addElementsToLeg(it)

            itineraryObject
                .addJsonElementProperty(pair.second, it)
                .removeProperty(pair.first)
        }
    }

    private fun addElementsToPricingOptions(pricingOptionsElements: JsonArray) {
        pricingOptionsElements.map { pricingOption ->
            pricingOption
                .asJsonObject
                .addJsonArrayOfComplexObjectsFromIds("agents", agentsReference, "Agents")
                .removeProperties(listOf("Agents", "QuoteAgeInMinutes", "DeeplinkUrl"))
        }
    }

    private fun addElementsToLeg(legObject: JsonObject) {
        val jsonObjectBatchItems = listOf(
            JsonObjectBatchItem(
                propertyName = "originStation",
                jsonArrayReference = placesReference,
                childNameWithId = "OriginStation"
            ),
            JsonObjectBatchItem(
                propertyName = "destinationStation",
                jsonArrayReference = placesReference,
                childNameWithId = "DestinationStation"
            )
        )

        val jsonArrayBatchItems = listOf(
            JsonArrayBatchItem(
                propertyName = "carriers",
                jsonArrayReference = carriersReference,
                nodeNameOfListId = "Carriers"
            ),
            JsonArrayBatchItem(
                propertyName = "operatingCarriers",
                jsonArrayReference = carriersReference,
                nodeNameOfListId = "OperatingCarriers"
            )
        )

        legObject
            .addJsonObjectsInBatch(jsonObjectBatchItems)
            .addJsonArrayInBatch(jsonArrayBatchItems)
            .addJsonElementProperty("segments", setupSegments(legObject))
            .removeProperties(
                listOf("OriginStation", "DestinationStation", "Carriers", "OperatingCarriers", "SegmentIds")
            )
    }

    private fun setupSegments(legObject: JsonObject): JsonArray {

        val jsonObjectBatchItems = listOf(
            JsonObjectBatchItem(
                propertyName = "originStation",
                jsonArrayReference = placesReference,
                childNameWithId = "OriginStation"
            ),
            JsonObjectBatchItem(
                propertyName = "destinationStation",
                jsonArrayReference = placesReference,
                childNameWithId = "DestinationStation"
            ),
            JsonObjectBatchItem(
                propertyName = "carrier",
                jsonArrayReference = carriersReference,
                childNameWithId = "Carrier"
            ),
            JsonObjectBatchItem(
                propertyName = "operatingCarrier",
                jsonArrayReference = carriersReference,
                childNameWithId = "OperatingCarrier"
            )
        )

        val segmentElements =
            legObject.getListOfJsonElementBasedOnListOfIds("SegmentIds", segmentsReference)

        return segmentElements.map { segmentElement ->
            segmentElement.asJsonObject
                .addJsonObjectsInBatch(jsonObjectBatchItems)
                .removeProperties(jsonObjectBatchItems.map { it.childNameWithId })
        }.toJsonArray()
    }
}

enum class LegType {
    OUTBOUND, INBOUND
}