package com.nexters.doctor24.todoc.di

import android.content.res.Resources
import com.nexters.doctor24.todoc.base.DefaultDispatcherProvider
import com.nexters.doctor24.todoc.base.DispatcherProvider
import com.nexters.doctor24.todoc.ui.map.NaverMapViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule = module {
    single { androidContext().resources as Resources }
    factory { DefaultDispatcherProvider() as DispatcherProvider }
    viewModel { NaverMapViewModel(get(), get(), get()) }
}