package com.haroldo.searchforflights.di

import android.app.Application
import com.haroldo.searchforflights.network.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@ApplicationScope
@Component(
    modules = [
        ApplicationModule::class,
        NetworkModule::class
    ]
)
interface ApplicationComponent : FlightsResultComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope