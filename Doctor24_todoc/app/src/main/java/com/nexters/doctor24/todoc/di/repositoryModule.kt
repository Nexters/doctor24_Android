package com.nexters.doctor24.todoc.di

import com.nexters.doctor24.todoc.repository.MarkerRepository
import com.nexters.doctor24.todoc.repository.MarkerRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory { MarkerRepositoryImpl(get()) as MarkerRepository }
}