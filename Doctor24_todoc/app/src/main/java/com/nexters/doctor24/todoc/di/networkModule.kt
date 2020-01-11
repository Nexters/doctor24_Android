package com.nexters.doctor24.todoc.di

import com.nexters.doctor24.todoc.BuildConfig
import com.nexters.doctor24.todoc.data.MarkerDataSource
import com.nexters.doctor24.todoc.data.MarkerDataSourceImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by jiyoung on 09/01/2020
 */
private val BASE_URL = "http://27.96.130.44:8080/api/v1"

val networkModule: Module = module {
    single(definition = {
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                        level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    })
                    .build())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    })

    single{ MarkerDataSourceImpl(get()) as MarkerDataSource }
}