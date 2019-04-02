package com.haroldo.searchforflights.flightsresults.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.haroldo.searchforflights.R
import com.haroldo.searchforflights.changeVisibility
import com.haroldo.searchforflights.di.FlightsResultComponentHolder
import com.haroldo.searchforflights.model.*
import com.haroldo.searchforflights.showSnackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import javax.inject.Inject


class MainActivity : AppCompatActivity(), FlightsResultView {

    @Inject
    lateinit var presenter: FlightsResultPresenter

    @Inject
    lateinit var resultsAdapter: FlightsResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        performInjections()

        presenter.onAttach(this)

        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = resultsAdapter
        }

        resultsAdapter.updateItems(
            listOf(EXPECTED_ITINERARY_OBJECT_0, EXPECTED_ITINERARY_OBJECT_1)
        )
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun showLoading() {
        loading.changeVisibility(true)
    }

    override fun hideLoading() {
        loading.changeVisibility(false)
    }

    override fun showCreateSessionError() {
        recyclerView.showSnackbar("Create session error", R.string.retry) {
            presenter.retrySession()
        }
    }

    override fun showFetchResultsError() {
        recyclerView.showSnackbar("Fetch results error", R.string.retry) {
            presenter.retryFetchResults()
        }
    }

    override fun updateItems(newItems: List<Itinerary>, diffResult: DiffUtil.DiffResult) {
//        resultsAdapter.updateItems(newItems, diffResult) {
//            recyclerView.showSnackbar("Feature not implemented yet")
//        }
    }

    private fun performInjections() {
        FlightsResultComponentHolder.getComponent(this).inject(this)
    }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    val EDI_PLACE = Place(
        id = 11235,
        code = "EDI",
        type = "Airport",
        name = "Edinburgh"
    )

    val LHR_PLACE = Place(
        id = 13554,
        code = "LHR",
        type = "Airport",
        name = "London Heathrow"
    )

    val DUB_PLACE = Place(
        id = 12344,
        code = "DUB",
        type = "City",
        name = "Dublin"
    )

    val RE_CARRIER = Carrier(
        id = 1653,
        code = "RE",
        name = "Stobart Air",
        imageUrl = "https://s1.apideeplink.com/images/airlines/RE.png",
        displayCode = "RE"
    )

    val EI_CARRIER = Carrier(
        id = 1033,
        code = "EI",
        name = "Aer Lingus",
        imageUrl = "https://s1.apideeplink.com/images/airlines/EI.png",
        displayCode = "EI"
    )

    val SEGMENT_0 =
        Segment(
            id = 87,
            origin = EDI_PLACE,
            destination = LHR_PLACE,
            departureTime = dateFormat.parse("2019-03-29T16:45:00"),
            arrivalTime = dateFormat.parse("2019-03-29T18:00:00"),
            carrier = RE_CARRIER,
            duration = 75,
            flightNumber = "3555"
        )

    val SEGMENT_1 =
        Segment(
            id = 88,
            origin = DUB_PLACE,
            destination = LHR_PLACE,
            departureTime = dateFormat.parse("2019-03-29T16:45:00"),
            arrivalTime = dateFormat.parse("2019-03-29T18:00:00"),
            carrier = RE_CARRIER,
            duration = 75,
            flightNumber = "3555"
        )

    fun givenLeg(
        id: String,
        origin: Place,
        destination: Place,
        carrier: Carrier,
        segments: List<Segment>,
        stopsAmount: Int
    ) = Leg(
        id = id,
        origin = origin,
        destination = destination,
        departureTime = dateFormat.parse("2019-03-29T16:45:00"),
        arrivalTime = dateFormat.parse("2019-03-29T22:05:00"),
        carrier = carrier,
        duration = 320,
        segments = segments,
        stopsAmount = stopsAmount
    )

    val EXPECTED_ITINERARY_OBJECT_0 = Itinerary(
        price = 293.58.toBigDecimal(),
        agentNames = listOf("British Airways", "GotoGate"),
        rating = "3.0",
        cheapest = true,
        outboundLeg = givenLeg(
            id = "outleg_0",
            origin = EDI_PLACE,
            destination = LHR_PLACE,
            carrier = RE_CARRIER,
            segments = listOf(SEGMENT_0, SEGMENT_1),
            stopsAmount = 4
        ),
        inboundLeg = givenLeg(
            id = "inleg_0",
            origin = LHR_PLACE,
            destination = EDI_PLACE,
            carrier = RE_CARRIER,
            segments = listOf(SEGMENT_1, SEGMENT_0),
            stopsAmount = 2
        )
    )

    val EXPECTED_ITINERARY_OBJECT_1 = Itinerary(
        price = 332.58.toBigDecimal(),
        agentNames = listOf("Expedia"),
        rating = "3.0",
        shortest = true,
        outboundLeg = givenLeg(
            id = "outleg_1",
            origin = DUB_PLACE,
            destination = EDI_PLACE,
            carrier = EI_CARRIER,
            segments = listOf(SEGMENT_0, SEGMENT_1),
            stopsAmount = 2
        ),
        inboundLeg = givenLeg(
            id = "inleg_1",
            origin = EDI_PLACE,
            destination = DUB_PLACE,
            carrier = RE_CARRIER,
            segments = listOf(SEGMENT_0, SEGMENT_1),
            stopsAmount = 0
        )
    )

}