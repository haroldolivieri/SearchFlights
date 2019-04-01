package com.haroldo.searchforflights.network

import android.content.SharedPreferences
import javax.inject.Inject

private const val URL_POLLING = "url_polling"

class PollingUrlProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun set(pollingUrl: String) = sharedPreferences.edit().putString(URL_POLLING, pollingUrl).apply()
    fun get(): String = sharedPreferences.getString(URL_POLLING, "")!!
}
