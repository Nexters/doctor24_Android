package com.nexters.doctor24.todoc.data.detailed

import com.nexters.doctor24.todoc.api.APIDetailed
import com.nexters.doctor24.todoc.data.detailed.response.DetailedInfoData
import retrofit2.Response
import retrofit2.Retrofit

internal class DetailedDataSourceImpl(val retrofit: Retrofit) : DetailedDataSource {
    override suspend fun getDetailedInfo(
        type: String,
        facilityId: String
    ): DetailedInfoData = retrofit.create(APIDetailed::class.java)
        .getDetailedInfo(
            type = type, facilityId = facilityId
        )
}