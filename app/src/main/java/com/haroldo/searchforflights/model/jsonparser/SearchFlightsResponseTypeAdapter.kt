package com.haroldo.searchforflights.model.jsonparser

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

private const val PARSED_ITINERARIES = "parsedItineraries"
private const val RAW_ITINERARIES = "Itineraries"
private const val RAW_SESSION_KEY = "SessionKey"

class SearchFlightsResponseTypeAdapter : TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
        val delegate = gson.getDelegateAdapter(this, type)
        val elementAdapter = gson.getAdapter(JsonElement::class.java)

        return object : TypeAdapter<T>() {

            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: T) {
                delegate.write(out, value)
            }

            @Throws(IOException::class)
            override fun read(jsonReader: JsonReader): T {
                var jsonElement = elementAdapter.read(jsonReader)

                if (jsonElement.isJsonObject) {
                    val jsonObject = jsonElement.asJsonObject

                    if (needsToParseRawResponse(jsonObject)) {
                        jsonObject.add(
                            PARSED_ITINERARIES,
                            parseItineraries(
                                jsonObject.getAsJsonArray(RAW_ITINERARIES),
                                jsonObject
                            )
                        )

                        jsonElement = jsonObject
                    }
                }

                return delegate.fromJsonTree(jsonElement)
            }

            private fun needsToParseRawResponse(jsonObject: JsonObject) = jsonObject.has(RAW_SESSION_KEY)

            private fun parseItineraries(itineraries: JsonArray, parentObject: JsonObject): JsonArray =
                itineraries.map { itineraryElement ->
                    itineraryElement.asJsonObject.run {
                        with(SearchFlightsJsonMapper(parentObject)) {
                            setupPricingOptions(this@run)
                            setupLeg(this@run, SearchFlightsJsonMapper.LegType.INBOUND)
                            setupLeg(this@run, SearchFlightsJsonMapper.LegType.OUTBOUND)
                            this@run
                        }
                    }
                }.toJsonArray()

        }.nullSafe()
    }
}