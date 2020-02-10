package com.nexters.doctor24.todoc.repository

import com.nexters.doctor24.todoc.data.detailed.DetailedDataSource
import com.nexters.doctor24.todoc.data.detailed.response.DetailedInfoData
import retrofit2.Response

internal class DetailedInfoRepositoryImpl(private val dataSource: DetailedDataSource) :
    DetailedInfoRepository {
    override suspend fun getDetailedInfo(type: String, facilityId: String): Response<DetailedInfoData> {
        return dataSource.getDetailedInfo(type = type, facilityId = facilityId)
    }
}