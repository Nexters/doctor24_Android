package com.nexters.doctor24.todoc.repository

import com.nexters.doctor24.todoc.data.detailed.response.DetailedInfoData
import retrofit2.Response

interface DetailedInfoRepository {
    suspend fun getDetailedInfo(type: String, facilityId: String) : DetailedInfoData
}