package com.haroldo.searchforflights.request

data class Event<T>(val type: Type, val data: T?, val error: Throwable?) {
    companion object {
        @JvmStatic
        fun <T> loading() = Event<T>(Type.LOADING, null, null)

        @JvmStatic
        fun <T> data(data: T) = Event(Type.HAS_DATA, data, null)

        @JvmStatic
        fun <T> error(error: Throwable): Event<T> = Event(Type.ERROR, null, error)

        @JvmStatic
        fun <T> completedWith(data: T): Event<T> = Event(Type.COMPLETED, data, null)
    }

    val isLoading: Boolean
        get() = type == Type.LOADING

    val isError: Boolean
        get() = type == Type.ERROR

    val hasData: Boolean
        get() = type == Type.HAS_DATA

    val isCompletedWithData: Boolean
        get() = type == Type.COMPLETED

    enum class Type {
        LOADING, HAS_DATA, ERROR, COMPLETED
    }
}