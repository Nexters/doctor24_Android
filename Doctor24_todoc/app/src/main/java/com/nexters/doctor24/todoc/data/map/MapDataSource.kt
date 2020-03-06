package com.nexters.doctor24.todoc.data.map

import com.nexters.doctor24.todoc.data.map.response.ResMapAddress
import retrofit2.Response

internal interface MapDataSource {
    suspend fun getAddress(coords: String): Response<ResMapAddress>
}