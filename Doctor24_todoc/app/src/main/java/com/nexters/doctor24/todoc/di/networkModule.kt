package com.nexters.doctor24.todoc.di

import com.nexters.doctor24.todoc.BuildConfig
import com.nexters.doctor24.todoc.api.DefaultAPIInfo
import com.nexters.doctor24.todoc.api.DefaultNaverAPIInfo
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
private val BASE_URL = "http://27.96.130.44:8080/api/${DefaultAPIInfo.API_VERSION}/"
private val NAVER_BASE_URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/${DefaultNaverAPIInfo.API_VERSION}/"

val networkModule: Module = module {
    single(named("todocApi")) {
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

    single(named("naverApi")) {
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                        level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    })
                    .addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader(
                                DefaultNaverAPIInfo.HEADER_CLIENT_ID,
                                DefaultNaverAPIInfo.HEADER_CLIENT_ID_VALUE
                            )
                            .addHeader(
                                DefaultNaverAPIInfo.HEADER_CLIENT_KEY,
                                DefaultNaverAPIInfo.HEADER_CLIENT_KEY_VALUE
                            )
                            .build()
                        chain.proceed(newRequest)
                    }
                    .build())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(NAVER_BASE_URL)
            .build()
    }

    single{ MarkerDataSourceImpl(get(named("todocApi"))) as MarkerDataSource }
}