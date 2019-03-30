package com.challenge.searchforflights.flightsresults.model.jsonparser

import com.google.gson.JsonArray
import com.google.gson.JsonObject

class SearchFlightsJsonMapper constructor(parentJsonObject: JsonObject) {

    private val legsReference: JsonArray = parentJsonObject.getAsJsonArray(LEGS_REFERENCE_CHILD_NAME)
    private val segmentsReference: JsonArray = parentJsonObject.getAsJsonArray(SEGMENTS_REFERENCE_CHILD_NAME)
    private val carriersReference: JsonArray = parentJsonObject.getAsJsonArray(CARRIERS_REFERENCE_CHILD_NAME)
    private val agentsReference: JsonArray = parentJsonObject.getAsJsonArray(AGENTS_REFERENCE_CHILD_NAME)
    private val placesReference: JsonArray = parentJsonObject.getAsJsonArray(PLACES_REFERENCE_CHILD_NAME)

    fun setupPricingOptions(itineraryObject: JsonObject) {
        itineraryObject.getAsJsonArray(RAW_PRICING_OPTIONS).deepCopy().let {
            addElementsToPricingOptions(it)

            itineraryObject
                .addJsonElementProperty(PARSED_PRICING_OPTIONS, it)
                .removeProperty(RAW_PRICING_OPTIONS)
        }
    }

    fun setupLeg(itineraryObject: JsonObject, legType: LegType) {
        val pair = if (legType == LegType.OUTBOUND) {
            Pair(INBOUND_LEG_ID_CHILD_NAME, PARSED_INBOUND_LEG)
        } else {
            Pair(OUTBOUND_LEG_ID_CHILD_NAME, PARSED_OUTBOUND_LEG)
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
                .addJsonArrayOfComplexObjectsFromIds(PARSED_AGENTS, agentsReference, AGENTS_REFERENCE_CHILD_NAME)
                .removeProperties(listOf(AGENTS_REFERENCE_CHILD_NAME, RAW_QUOTE_AGE_IN_MINUTES, RAW_DEEPLINK))
        }
    }

    private fun addElementsToLeg(legObject: JsonObject) {
        val jsonObjectBatchItems = listOf(
            JsonObjectBatchItem(
                propertyName = PARSED_ORIGIN_STATION,
                jsonArrayReference = placesReference,
                childNameWithId = RAW_ORIGIN_STATION
            ),
            JsonObjectBatchItem(
                propertyName = PARSED_DESTINATION_STATION,
                jsonArrayReference = placesReference,
                childNameWithId = RAW_DESTINATION_STATION
            )
        )

        val jsonArrayBatchItems = listOf(
            JsonArrayBatchItem(
                propertyName = PARSED_CARRIERS,
                jsonArrayReference = carriersReference,
                nodeNameOfListId = RAW_CARRIERS
            ),
            JsonArrayBatchItem(
                propertyName = PARSED_OPERATING_CARRIERS,
                jsonArrayReference = carriersReference,
                nodeNameOfListId = RAW_OPERATING_CARRIERS
            )
        )

        legObject
            .addJsonObjectsInBatch(jsonObjectBatchItems)
            .addJsonArrayInBatch(jsonArrayBatchItems)
            .addJsonElementProperty(PARSED_SEGMENTS, setupSegments(legObject))
            .removeProperties(
                listOf(
                    RAW_ORIGIN_STATION,
                    RAW_DESTINATION_STATION,
                    RAW_CARRIERS,
                    RAW_OPERATING_CARRIERS,
                    SEGMENTS_ID_CHILD_NAME
                )
            )
    }

    private fun setupSegments(legObject: JsonObject): JsonArray {

        val jsonObjectBatchItems = listOf(
            JsonObjectBatchItem(
                propertyName = PARSED_ORIGIN_STATION,
                jsonArrayReference = placesReference,
                childNameWithId = RAW_ORIGIN_STATION
            ),
            JsonObjectBatchItem(
                propertyName = PARSED_DESTINATION_STATION,
                jsonArrayReference = placesReference,
                childNameWithId = RAW_DESTINATION_STATION
            ),
            JsonObjectBatchItem(
                propertyName = PARSED_CARRIER,
                jsonArrayReference = carriersReference,
                childNameWithId = RAW_CARRIER
            ),
            JsonObjectBatchItem(
                propertyName = PARSED_OPERATING_CARRIER,
                jsonArrayReference = carriersReference,
                childNameWithId = RAW_OPERATING_CARRIER
            )
        )

        val segmentElements =
            legObject.getListOfJsonElementBasedOnListOfIds(SEGMENTS_ID_CHILD_NAME, segmentsReference)

        return segmentElements.map { segmentElement ->
            segmentElement.asJsonObject
                .addJsonObjectsInBatch(jsonObjectBatchItems)
                .removeProperties(jsonObjectBatchItems.map { it.childNameWithId })
        }.toJsonArray()
    }

    enum class LegType {
        OUTBOUND, INBOUND
    }

    private companion object {
        const val LEGS_REFERENCE_CHILD_NAME = "Legs"
        const val SEGMENTS_REFERENCE_CHILD_NAME = "Segments"
        const val CARRIERS_REFERENCE_CHILD_NAME = "Carriers"
        const val AGENTS_REFERENCE_CHILD_NAME = "Agents"
        const val PLACES_REFERENCE_CHILD_NAME = "Places"

        const val INBOUND_LEG_ID_CHILD_NAME = "InboundLegId"
        const val OUTBOUND_LEG_ID_CHILD_NAME = "OutboundLegId"
        const val SEGMENTS_ID_CHILD_NAME = "SegmentIds"

        const val RAW_PRICING_OPTIONS = "PricingOptions"
        const val RAW_CARRIERS = "Carriers"
        const val RAW_OPERATING_CARRIERS = "OperatingCarriers"
        const val RAW_CARRIER = "Carrier"
        const val RAW_OPERATING_CARRIER = "OperatingCarrier"
        const val RAW_ORIGIN_STATION = "OriginStation"
        const val RAW_DESTINATION_STATION = "DestinationStation"
        const val RAW_QUOTE_AGE_IN_MINUTES = "QuoteAgeInMinutes"
        const val RAW_DEEPLINK = "DeeplinkUrl"

        const val PARSED_PRICING_OPTIONS = "pricingOptions"
        const val PARSED_OUTBOUND_LEG = "outboundLeg"
        const val PARSED_INBOUND_LEG = "inboundLeg"
        const val PARSED_OPERATING_CARRIER = "operatingCarrier"
        const val PARSED_CARRIER = "carrier"
        const val PARSED_OPERATING_CARRIERS = "operatingCarriers"
        const val PARSED_CARRIERS = "carriers"
        const val PARSED_ORIGIN_STATION = "originStation"
        const val PARSED_DESTINATION_STATION = "destinationStation"
        const val PARSED_AGENTS = "agents"
        const val PARSED_SEGMENTS = "segments"
    }
}