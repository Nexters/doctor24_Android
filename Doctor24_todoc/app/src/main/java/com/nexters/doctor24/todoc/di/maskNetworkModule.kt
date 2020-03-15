package com.nexters.doctor24.todoc.di

import com.nexters.doctor24.todoc.BuildConfig
import com.nexters.doctor24.todoc.data.mask.MaskStoreDataSource
import com.nexters.doctor24.todoc.data.mask.MaskStoreDataSourceImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val BASE_URL = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/"

val maskNetworkModule: Module = module {
    single(named("mask")) {
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                        level =
                            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    })
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    single{ MaskStoreDataSourceImpl(get()) as MaskStoreDataSource }
}

