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
interface FlightsResultSubComponent {
    fun inject(activity: MainActivity)
}

interface FlightsResultComponent {
    val flightsResultComponent: FlightsResultSubComponent
}

class FlightsResultComponentHolder(app: Application) : AndroidViewModel(app) {

    companion object {
        fun getComponent(activity: FragmentActivity) = ViewModelProviders
            .of(activity)
            .get(FlightsResultComponentHolder::class.java)
            .getCachedComponent()
    }

    private var component: FlightsResultSubComponent? = null

    private fun getCachedComponent(): FlightsResultSubComponent {
        if (component == null) {
            component = (getApplication<Application>() as FlightsResultComponentProvider)
                .component()
                .flightsResultComponent
        }
        return component!!
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FlightsResultScope