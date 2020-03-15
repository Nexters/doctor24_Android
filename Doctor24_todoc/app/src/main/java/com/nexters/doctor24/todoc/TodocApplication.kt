package com.nexters.doctor24.todoc

import android.app.Application
import com.naver.maps.map.NaverMapSdk
import com.nexters.doctor24.todoc.api.DefaultNaverAPIInfo
import com.nexters.doctor24.todoc.di.*
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

        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(DefaultNaverAPIInfo.HEADER_CLIENT_ID_VALUE)

        startKoin {
            androidContext(this@TodocApplication)
            modules(listOf(networkModule, dbModule, appModule, repositoryModule, preferencesModule, maskNetworkModule))
        }
    }
}