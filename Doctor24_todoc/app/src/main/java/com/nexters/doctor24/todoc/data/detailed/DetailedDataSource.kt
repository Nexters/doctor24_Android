package com.nexters.doctor24.todoc.data.detailed

import com.nexters.doctor24.todoc.data.detailed.response.DetailedInfoData
import retrofit2.Response

internal interface DetailedDataSource {
    suspend fun getDetailedInfo(type: String, facilityId: String): DetailedInfoData
}