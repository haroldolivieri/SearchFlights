package com.haroldo.searchforflights.request

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.disposables.DisposableHelper
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class InMemoryFlowableRequest<T>(private val source: Flowable<T>) {

    private val initialState: Event<T> =
        Event.loading()
    private val eventsSubject = BehaviorSubject.createDefault(initialState)
    private val isInitialState: Boolean get() = eventsSubject.value === initialState

    private val disposable = CompositeDisposable()

    fun events(): Observable<Event<T>> =
        eventsSubject.doOnSubscribe {
            if (isInitialState) {
                subscribe(source)
            }
        }

    fun retry() {
        dispose()
        subscribe(source)
    }

    fun dispose() {
        disposable.clear()
    }

    private fun subscribe(source: Flowable<T>) {
        source
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onLoading() }
            .subscribe({
                onData(it)
            }, {
                onError(it)
            }, {
                eventsSubject.value?.data?.let { onCompleteWith(it) }
            }).addTo(disposable)
    }

    private fun onLoading() {
        eventsSubject.onNext(Event.loading())
    }

    private fun onData(data: T) {
        eventsSubject.onNext(Event.data(data))
    }

    private fun onCompleteWith(data: T) {
        eventsSubject.onNext(Event.completedWith(data))
    }

    private fun onError(throwable: Throwable) {
        eventsSubject.onNext(Event.error(throwable))
    }
}
