package com.haroldo.searchforflights.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

@Module
class ApplicationModule {

    @Provides
    @ApplicationScope
    fun provideContext(application: Application): Context = application

    @Provides
    @ApplicationScope
    fun provideSharedPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * This apiKey in a real project should not be commited.
     * I would add this into a separate file and component the value from BuildConfig
     */
    @Provides
    @ApiKey
    fun provideApiKey(): String = "API_KEY"
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiKey