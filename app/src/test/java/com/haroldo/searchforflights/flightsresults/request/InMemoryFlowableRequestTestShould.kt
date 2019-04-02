package com.haroldo.searchforflights.flightsresults.request

import com.google.common.truth.Truth.assertThat
import com.haroldo.searchforflights.flightsresults.RxTestRule
import com.haroldo.searchforflights.request.Event
import com.haroldo.searchforflights.request.InMemoryFlowableRequest
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import org.junit.Rule
import org.junit.Test

class InMemoryFlowableRequestTestShould {

    @Rule
    @JvmField
    val rule = RxTestRule()

    private val request = InMemoryFlowableRequest(getFlowable())
    private lateinit var emitter: FlowableEmitter<List<Int>>
    private var subscriptionCounter = 0
    private val error = Throwable("error")

    @Test
    fun `starts with loading`() {
        val test = request.events().test()

        test.assertValue(Event.loading())
    }

    @Test
    fun `receive data`() {
        val test = request.events().test()

        emitter.onNext(listOf(1, 2, 3, 4))

        test.assertValues(Event.loading(), Event.data(listOf(1, 2, 3, 4)))
    }

    @Test
    fun `receive error`() {
        val test = request.events().test()

        emitter.onError(error)

        test.assertValues(Event.loading(), Event.error(error))
    }

    @Test
    fun `return cached event emitted on new subscription`() {
        request.events().test()

        emitter.onNext(listOf(4, 3, 2, 1))
        emitter.onNext(listOf(1, 2, 3, 4))

        val newSubscriber = request.events().test()
        newSubscriber.assertValue(Event.data(listOf(1, 2, 3, 4)))
    }

    @Test
    fun `calling request twice should return cached data even after completion`() {
        val firstSubscriber = request.events().test()
        emitter.onNext(listOf(1, 2, 3, 4))
        emitter.onComplete()

        firstSubscriber.assertValues(
            Event.loading(),
            Event.data(listOf(1, 2, 3, 4)),
            Event.completedWith(listOf(1, 2, 3, 4))
        )

        val secondSubscriber = request.events().test()

        secondSubscriber.assertValue(Event.completedWith(listOf(1, 2, 3, 4)))
    }

    @Test
    fun `complete should only emit after event with data`() {
        val firstSubscriber = request.events().test()

        emitter.onError(error)
        emitter.onComplete()

        firstSubscriber.assertValues(
            Event.loading(),
            Event.error(error)
        )
    }

    @Test
    fun `subscribe only once`() {
        for (i in 0..100) {
            request.events().subscribe()
        }

        assertThat(subscriptionCounter).isEqualTo(1)
    }

    @Test
    fun `retry resubscribes`() {
        request.events().subscribe()
        request.retry()

        assertThat(subscriptionCounter).isEqualTo(2)
    }

    @Test
    fun `emits loading again after retry`() {
        val test = request.events().test()

        emitter.onNext(listOf(1, 2, 3))
        request.retry()
        emitter.onNext(listOf(4, 5, 6))

        test.assertValues(
            Event.loading(),
            Event.data(listOf(1, 2, 3)),
            Event.loading(),
            Event.data(listOf(4, 5, 6))
        )
    }

    @Test
    fun `work correctly with multiple retries`() {
        val test = request.events().test()

        emitter.onError(error)

        request.retry()
        emitter.onNext(listOf(1, 2, 3))

        request.retry()
        emitter.onNext(listOf(4, 5, 6))

        request.retry()
        emitter.onNext(listOf(7, 8, 9))

        request.retry()
        emitter.onError(error)

        test.assertValues(
            Event.loading(), Event.error(error),
            Event.loading(), Event.data(listOf(1, 2, 3)),
            Event.loading(), Event.data(listOf(4, 5, 6)),
            Event.loading(), Event.data(listOf(7, 8, 9)),
            Event.loading(), Event.error(error)
        )
    }

    private fun getFlowable(): Flowable<List<Int>> {
        return Flowable.create({ e ->
            subscriptionCounter++
            emitter = e
        }, BackpressureStrategy.LATEST)
    }
}