package com.challenge.searchforflights.di

import android.app.Application
import com.challenge.searchforflights.FlightsApplication
import dagger.Component
import javax.inject.Scope

@Component
@ApplicationScope
interface ApplicationComponent {
    fun inject(app: FlightsApplication)
    fun application(): Application
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope