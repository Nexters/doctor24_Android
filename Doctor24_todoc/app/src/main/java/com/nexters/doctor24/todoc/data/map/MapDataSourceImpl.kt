package com.nexters.doctor24.todoc.data.map

import com.nexters.doctor24.todoc.api.APIMap
import com.nexters.doctor24.todoc.data.map.response.ResMapAddress
import retrofit2.Response
import retrofit2.Retrofit

internal class MapDataSourceImpl(val retrofit: Retrofit) : MapDataSource {
    override suspend fun getAddress(coords: String): Response<ResMapAddress>
            = retrofit.create(APIMap::class.java).getMapAddress(coords = coords, output = "json")
}