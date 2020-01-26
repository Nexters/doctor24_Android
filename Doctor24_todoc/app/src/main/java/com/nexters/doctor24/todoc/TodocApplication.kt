package com.nexters.doctor24.todoc

import android.app.Application
import com.naver.maps.map.NaverMapSdk
import com.nexters.doctor24.todoc.di.appModule
import com.nexters.doctor24.todoc.di.dbModule
import com.nexters.doctor24.todoc.di.networkModule
import com.nexters.doctor24.todoc.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created by jiyoung on 07/01/2020
 */
class TodocApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@TodocApplication)
            modules(listOf(networkModule, dbModule, appModule, repositoryModule))
        }
    }
}