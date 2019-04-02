package com.haroldo.searchforflights.request

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.disposables.DisposableHelper
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class InMemoryCompletableRequest(private val source: Completable) {

    private val initialState: Event<Unit> = Event.loading()
    private val eventsSubject = BehaviorSubject.createDefault(initialState)
    private val isInitialState: Boolean get() = eventsSubject.value === initialState

    private val disposable = CompositeDisposable()

    fun events() =
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

    private fun subscribe(source: Completable) {
        source
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onLoading() }
            .subscribe({
                onCompletedWithoutData()
            }, {
                onError(it)
            }).addTo(disposable)
    }

    private fun onLoading() {
        eventsSubject.onNext(Event.loading())
    }

    private fun onCompletedWithoutData() {
        eventsSubject.onNext(Event.completedWithoutData())
    }

    private fun onError(throwable: Throwable) {
        eventsSubject.onNext(Event.error(throwable))
    }
}
