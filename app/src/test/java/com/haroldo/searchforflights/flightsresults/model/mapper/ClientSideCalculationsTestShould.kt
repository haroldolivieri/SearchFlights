package com.haroldo.searchforflights.flightsresults.model.mapper

import com.haroldo.searchforflights.flightsresults.model.Carrier
import com.haroldo.searchforflights.flightsresults.model.Itinerary
import com.haroldo.searchforflights.flightsresults.model.Leg
import com.haroldo.searchforflights.flightsresults.model.Place
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.text.SimpleDateFormat

class ClientSideCalculationsTestShould {

    @Test
    fun `calculate client side values`() {

        itineraries.clientSideCalculations().forEachIndexed { index, itinerary ->
            with(itinerary) {
                assertThat(cheapest).isEqualTo(expectedResult[index].cheapest)
                assertThat(shortest).isEqualTo(expectedResult[index].shortest)
                assertThat(rating).isEqualTo(expectedResult[index].rating)
            }
        }
    }

    private companion object {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        val expectedResult = listOf(
            Validation(
                shortest = true,
                rating = "5.7"
            ),
            Validation(
                shortest = true,
                rating = "7.4"
            ),
            Validation(
                rating = "7.3"
            ),
            Validation(
                rating = "7.4"
            ),
            Validation(
                cheapest = true,
                rating = "3.3"
            )
        )

        data class Validation(
            val cheapest: Boolean = false,
            val shortest: Boolean = false,
            val rating: String
        )

        val itineraries = listOf(
            //price rating 0
            //duration rating 10
            //stops rating 7
            //rating -> 5.7
            Itinerary(
                price = 293.58.toBigDecimal(),
                agentNames = emptyList(),
                outboundLeg = givenLeg(
                    duration = 20,
                    stopsAmount = 1
                ),
                inboundLeg = givenLeg(
                    duration = 50,
                    stopsAmount = 3
                )
            ),
            //price rating 2.2
            //duration rating 10
            //stops rating 10
            //rating -> 7.4
            Itinerary(
                price = 230.toBigDecimal(),
                agentNames = emptyList(),
                outboundLeg = givenLeg(
                    duration = 50,
                    stopsAmount = 0
                ),
                inboundLeg = givenLeg(
                    duration = 20,
                    stopsAmount = 1
                )
            ),
            //price rating 2.2
            //duration rating 9.75
            //stops rating 10
            //rating -> 7.3
            Itinerary(
                price = 230.toBigDecimal(),
                agentNames = emptyList(),
                outboundLeg = givenLeg(
                    duration = 50,
                    stopsAmount = 0
                ),
                inboundLeg = givenLeg(
                    duration = 80,
                    stopsAmount = 1
                )
            ),
            //price rating 6
            //duration rating 9.25
            //stops rating 6.8
            //rating -> 7.4
            Itinerary(
                price = 100.toBigDecimal(),
                agentNames = emptyList(),
                outboundLeg = givenLeg(
                    duration = 200,
                    stopsAmount = 4
                ),
                inboundLeg = givenLeg(
                    duration = 50,
                    stopsAmount = 1
                )
            ),
            //price rating 10
            //duration rating 0
            //stops rating 0
            //rating -> 3.3
            Itinerary(
                price = 10.toBigDecimal(),
                agentNames = emptyList(),
                outboundLeg = givenLeg(
                    duration = 2000,
                    stopsAmount = 5
                ),
                inboundLeg = givenLeg(
                    duration = 500,
                    stopsAmount = 6
                )
            )
        )

        fun givenLeg(
            duration: Int,
            stopsAmount: Int
        ) = Leg(
            id = "id",
            origin = Place(
                id = 12344,
                code = "DUB",
                type = "City",
                name = "Dublin"
            ),
            destination = Place(
                id = 11235,
                code = "EDI",
                type = "Airport",
                name = "Edinburgh"
            ),
            departureTime = dateFormat.parse("2019-03-29T16:45:00"),
            arrivalTime = dateFormat.parse("2019-03-29T22:05:00"),
            carrier = Carrier(
                id = 1033,
                code = "EI",
                name = "Aer Lingus",
                imageUrl = "https://s1.apideeplink.com/images/airlines/EI.png",
                displayCode = "EI"
            ),
            duration = duration,
            segments = emptyList(),
            stopsAmount = stopsAmount
        )
    }
}