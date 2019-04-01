package com.haroldo.searchforflights.di

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProviders
import com.haroldo.searchforflights.FlightsResultComponentProvider
import com.haroldo.searchforflights.flightsresults.presentation.MainActivity
import dagger.Subcomponent
import javax.inject.Scope

@FlightsResultScope
@Subcomponent
interface FlightsResultComponent {
    fun inject(activity: MainActivity)
}

interface FlightsResultSubComponent {
    val flightsResultComponent: FlightsResultComponent
}

class FlightsResultComponentHolder(app: Application) : AndroidViewModel(app) {

    companion object {
        fun getComponent(activity: FragmentActivity) = ViewModelProviders
            .of(activity)
            .get(FlightsResultComponentHolder::class.java)
            .component
            .flightsResultComponent
    }

    private val component =
        (app as FlightsResultComponentProvider).component()
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FlightsResultScope