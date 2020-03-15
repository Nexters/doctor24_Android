package com.nexters.doctor24.todoc.di

import com.nexters.doctor24.todoc.BuildConfig
import com.nexters.doctor24.todoc.api.DefaultAPIInfo
import com.nexters.doctor24.todoc.data.detailed.DetailedDataSource
import com.nexters.doctor24.todoc.data.detailed.DetailedDataSourceImpl
import com.nexters.doctor24.todoc.data.marker.MarkerDataSource
import com.nexters.doctor24.todoc.data.marker.MarkerDataSourceImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by jiyoung on 09/01/2020
 */
private val BASE_URL = "http://todoc.me/api/${DefaultAPIInfo.API_VERSION}/"

val networkModule: Module = module {
    single(named("todoc")) {
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
    }

    single{ MarkerDataSourceImpl(get(named("todoc"))) as MarkerDataSource }
    single{ DetailedDataSourceImpl(get(named("todoc"))) as DetailedDataSource }
}