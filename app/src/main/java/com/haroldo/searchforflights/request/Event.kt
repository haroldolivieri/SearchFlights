package com.haroldo.searchforflights.request

data class Event<T>(val type: EventType, val data: T?, val error: Throwable?) {
    companion object {
        @JvmStatic
        fun <T> loading() = Event<T>(
            EventType.LOADING,
            null,
            null
        )

        @JvmStatic
        fun <T> data(data: T) = Event(
            EventType.DATA,
            data,
            null
        )

        @JvmStatic
        fun <T> error(error: Throwable): Event<T> =
            Event(
                EventType.ERROR,
                null,
                error
            )

        fun <T> completedWith(data: T): Event<T> =
            Event(
                EventType.COMPLETED,
                data,
                null
            )
    }

    enum class EventType {
        LOADING, DATA, ERROR, COMPLETED
    }
}