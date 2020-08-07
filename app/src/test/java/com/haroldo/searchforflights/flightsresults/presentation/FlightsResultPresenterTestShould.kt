package com.haroldo.searchforflights.flightsresults.presentation

import com.haroldo.searchforflights.flightsresults.interactor.CreateSessionInteractor
import com.haroldo.searchforflights.flightsresults.interactor.SearchFlightsInteractor
import com.haroldo.searchforflights.model.Itinerary
import com.haroldo.searchforflights.request.Event
import com.nhaarman.mockito_kotlin.*
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test

class FlightsResultPresenterTestShould {

    private val subjectSession = BehaviorSubject.create<Event<Unit>>()
    private val createSessionInteractor: CreateSessionInteractor = mock {
        on { events(any()) }.thenReturn(subjectSession)
    }

    private val subjectResults = BehaviorSubject.create<Event<Pair<List<Itinerary>, Boolean>>>()
    private val resultsInteractor: SearchFlightsInteractor = mock {
        on { events() }.thenReturn(subjectResults)
    }

    private val view: FlightsResultView = mock()
    private val presenter = FlightsResultPresenter(resultsInteractor, createSessionInteractor)

    @Test
    fun `show loading when loading event received`() {
        subjectSession.onNext(Event.loading())

        presenter.onAttach(view)

        verify(view).showLoading()
    }

    @Test
    fun `hide loading and show create session error when error event received from session interactor`() {
        presenter.onAttach(view)

        subjectSession.onNext(Event.error(Throwable()))

        verify(view).hideLoading()
        verify(view).showCreateSessionError()
    }

    @Test
    fun `do not hide loading after completed event received from session interactor`() {
        presenter.onAttach(view)

        subjectSession.onNext(Event.completedWithoutData())

        verify(view, never()).hideLoading()
    }

    @Test
    fun `not show loading when not subscribed yet`() {
        subjectResults.onNext(Event.loading())

        presenter.onAttach(view)

        verify(view, never()).showLoading()
    }

    @Test
    fun `hide loading and show results error when error event received from search flights interactor`() {
        presenter.onAttach(view)

        subjectSession.onNext(Event.completedWithoutData())
        subjectResults.onNext(Event.error(Throwable()))

        verify(view).hideLoading()
        verify(view).showFetchResultsError()
    }

    @Test
    fun `do not hide loading and update items after data event received from search flights interactor`() {
        presenter.onAttach(view)

        subjectSession.onNext(Event.completedWithoutData())
        subjectResults.onNext(Event.data(Pair(itineraries, false)))

        verify(view, never()).hideLoading()
        verify(view).updateItems(eq(itineraries + listOf(null)))
    }

    @Test
    fun `hide loading and update items after data completed with data event received from search flights interactor`() {
        presenter.onAttach(view)

        subjectSession.onNext(Event.completedWithoutData())
        subjectResults.onNext(Event.completedWith(Pair(itineraries, false)))

        verify(view).hideLoading()
        verify(view).updateItems(eq(itineraries + listOf(null)))
    }

    @Test
    fun `call retry on create interactor when retrySession is called`() {
        presenter.retrySession()

        verify(createSessionInteractor).retry()
    }

    @Test
    fun `call retry on search flights interactor when retryFetchResults is called`() {
        presenter.retryFetchResults()

        verify(resultsInteractor).retry()
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