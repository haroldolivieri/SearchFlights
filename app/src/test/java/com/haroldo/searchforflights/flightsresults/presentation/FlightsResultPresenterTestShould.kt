package com.haroldo.searchforflights.flightsresults.presentation

import com.haroldo.searchforflights.flightsresults.interactor.SearchFlightsInteractor
import com.haroldo.searchforflights.model.Itinerary
import com.haroldo.searchforflights.request.Event
import com.nhaarman.mockito_kotlin.*
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test

class FlightsResultPresenterTestShould {

    private val subject = BehaviorSubject.create<Event<List<Itinerary>>>()
    private val interactor: SearchFlightsInteractor = mock {
        on { events() }.thenReturn(subject)
    }

    private val view: FlightsResultView = mock()
    private val presenter = FlightsResultPresenter(interactor)

    @Test
    fun `show loading when loading event received`() {
        subject.onNext(Event.loading())

        presenter.onAttach(view)

        verify(view).showLoading()
    }

    @Test
    fun `hide loading and show error when error event received`() {
        presenter.onAttach(view)

        subject.onNext(Event.error(Throwable()))

        verify(view).hideLoading()
        verify(view).showGenericError()
    }

    @Test
    fun `hide loading and show data when completed event received`() {
        presenter.onAttach(view)

        subject.onNext(Event.completedWith(itineraries))

        verify(view).hideLoading()
        verify(view).updateItems(eq(itineraries), any())
    }

    @Test
    fun `show data when completed data event received`() {
        presenter.onAttach(view)

        subject.onNext(Event.data(itineraries))

        verify(view, never()).hideLoading()
        verify(view).updateItems(eq(itineraries), any())
    }

    @Test
    fun `call retry on interactor when retry is called`() {
        presenter.retry()

        verify(interactor).retry()
    }

    private val itineraries = listOf(
        givenItinerary(
            price = 100,
            rating = "3.4",
            cheapest = true,
            shortest = false
        ),
        givenItinerary(
            price = 120,
            rating = "5.8",
            cheapest = false,
            shortest = true
        )
    )

    private fun givenItinerary(
        price: Int,
        rating: String,
        cheapest: Boolean,
        shortest: Boolean
    ) = Itinerary(
        agentNames = emptyList(),
        outboundLeg = mock(),
        inboundLeg = mock(),
        price = price.toBigDecimal(),
        rating = rating,
        cheapest = cheapest,
        shortest = shortest
    )
}