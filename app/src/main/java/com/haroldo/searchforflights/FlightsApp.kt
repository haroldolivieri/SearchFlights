package com.haroldo.searchforflights

import android.app.Application
import com.haroldo.searchforflights.di.ApplicationComponent
import com.haroldo.searchforflights.di.DaggerApplicationComponent
import com.haroldo.searchforflights.di.FlightsResultComponent

class FlightsApp : Application(), FlightsResultComponentProvider {

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
            .application(this)
            .build()
    }

    override fun component(): FlightsResultComponent = applicationComponent

    private companion object {
        lateinit var applicationComponent: ApplicationComponent
    }
}

interface FlightsResultComponentProvider {
    fun component(): FlightsResultComponent
}