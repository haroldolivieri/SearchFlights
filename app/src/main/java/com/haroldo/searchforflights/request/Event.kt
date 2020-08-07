package com.haroldo.searchforflights.request

data class Event<T>(val type: Type, val data: T?, val error: Throwable?) {
    companion object {
        @JvmStatic
        fun <T> loading() = Event<T>(Type.LOADING, null, null)

        @JvmStatic
        fun <T> data(data: T) = Event(Type.IDLE, data, null)

        @JvmStatic
        fun <T> error(error: Throwable): Event<T> = Event(Type.ERROR, null, error)

        @JvmStatic
        fun <T> completedWith(data: T): Event<T> = Event(Type.COMPLETED, data, null)

        @JvmStatic
        fun completedWithoutData(): Event<Unit> = Event(Type.COMPLETED, null, null)
    }

    enum class Type {
        LOADING, IDLE, ERROR, COMPLETED
    }
}

inline fun <T> Event<T>.reducer(
    onLoading: () -> Unit = {},
    onError: () -> Unit = {},
    onDataArrived: (data: T) -> Unit = {},
    onCompleted: (data : T?) -> Unit = {}
) {
    when (type) {
        Event.Type.LOADING -> { onLoading() }
        Event.Type.ERROR -> { onError() }
        Event.Type.IDLE -> { onDataArrived(data!!)}
        Event.Type.COMPLETED -> { onCompleted(data)}
    }
}