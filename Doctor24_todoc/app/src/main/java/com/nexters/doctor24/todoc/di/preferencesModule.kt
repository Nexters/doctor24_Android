package com.nexters.doctor24.todoc.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

val preferencesModule = module {
    single(named("filterPrefs")) { provideSettingsPreferences(androidApplication()) }
}

private const val PREFERENCES_FILE_KEY = "com.nexters.doctor24.todoc.filter"

private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)