package com.nexters.doctor24.todoc.di

import com.nexters.doctor24.todoc.repository.MapRepository
import com.nexters.doctor24.todoc.repository.MapRepositoryImpl
import com.nexters.doctor24.todoc.repository.MarkerRepository
import com.nexters.doctor24.todoc.repository.MarkerRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory { MarkerRepositoryImpl(get()) as MarkerRepository }
    factory { MapRepositoryImpl(get()) as MapRepository }
}