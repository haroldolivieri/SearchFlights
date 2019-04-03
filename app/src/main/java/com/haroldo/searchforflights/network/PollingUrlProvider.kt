package com.haroldo.searchforflights.network

import android.content.SharedPreferences
import com.haroldo.searchforflights.di.ApiKey
import com.haroldo.searchforflights.di.ApplicationScope
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

private const val URL_POLLING = "url_polling"

@ApplicationScope
class PollingUrlProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun set(pollingUrl: String) {
        if (!pollingUrl.isEmpty()) {
            sharedPreferences.edit().putString(URL_POLLING, pollingUrl).apply()
        }
    }

    fun get(): String = sharedPreferences.getString(URL_POLLING, "")!!
}
