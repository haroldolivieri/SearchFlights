package com.challenge.searchforflights.flightsresults.model.jsonparser

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class SearchFlightsTypeAdapter : TypeAdapterFactory {

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
                    val jsonObject = jsonElement.asJsonObject.deepCopy()

                    if (jsonObject.has("SessionKey")) {
                        jsonObject.add(
                            "itineraries",
                            parseItineraries(
                                jsonObject.getAsJsonArray("Itineraries"),
                                jsonObject
                            )
                        )

                        jsonElement = jsonObject
                    }
                }

                return delegate.fromJsonTree(jsonElement)
            }

            private fun parseItineraries(itineraries: JsonArray, parentObject: JsonObject): JsonArray =
                itineraries.map { itineraryElement ->
                    itineraryElement.asJsonObject.run {
                        with(SearchFlightsJsonMapper(parentObject)) {
                            setupPricingOptions(this@run)
                            setupLeg(this@run, LegType.INBOUND)
                            setupLeg(this@run, LegType.OUTBOUND)
                            this@run
                        }
                    }
                }.toJsonArray()

        }.nullSafe()
    }
}