package com.nexters.doctor24.todoc.di

import android.content.res.Resources
import com.nexters.doctor24.todoc.base.DefaultDispatcherProvider
import com.nexters.doctor24.todoc.base.DispatcherProvider
import com.nexters.doctor24.todoc.ui.mask.MaskMapViewModel
import com.nexters.doctor24.todoc.ui.detailed.DetailedViewModel
import com.nexters.doctor24.todoc.ui.findload.FindLoadViewModel
import com.nexters.doctor24.todoc.ui.map.NaverMapViewModel
import com.nexters.doctor24.todoc.ui.map.TimeViewModel
import com.nexters.doctor24.todoc.ui.map.category.CategoryViewModel
import com.nexters.doctor24.todoc.ui.map.list.MedicalListViewModel
import com.nexters.doctor24.todoc.ui.map.preview.PreviewViewModel
import com.nexters.doctor24.todoc.ui.mask.preview.PreviewMaskViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single { androidContext().resources as Resources }
    factory { DefaultDispatcherProvider() as DispatcherProvider }
    viewModel { NaverMapViewModel(get(), get(named("filterPrefs")), get(), get()) }
    viewModel { TimeViewModel(get()) }
    viewModel { CategoryViewModel() }
    viewModel { DetailedViewModel(get(),get()) }
    viewModel { PreviewViewModel() }
    viewModel { PreviewMaskViewModel() }
    viewModel { FindLoadViewModel() }
    viewModel { MedicalListViewModel() }
    viewModel { MaskMapViewModel(get(), get(named("filterPrefs")), get()) }
}