package com.nexters.doctor24.todoc.di

import com.nexters.doctor24.todoc.repository.*
import com.nexters.doctor24.todoc.repository.DetailedInfoRepository
import com.nexters.doctor24.todoc.repository.DetailedInfoRepositoryImpl
import com.nexters.doctor24.todoc.repository.MarkerRepository
import com.nexters.doctor24.todoc.repository.MarkerRepositoryImpl
import com.nexters.doctor24.todoc.repository.MaskStoreRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory { MarkerRepositoryImpl(get()) as MarkerRepository }
    factory { DetailedInfoRepositoryImpl(get()) as DetailedInfoRepository }
    factory { MaskStoreRepositoryImpl(get()) as MaskStoreRepository }
}