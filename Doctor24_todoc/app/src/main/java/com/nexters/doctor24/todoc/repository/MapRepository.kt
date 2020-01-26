package com.nexters.doctor24.todoc.repository

import com.nexters.doctor24.todoc.base.Result
import com.nexters.doctor24.todoc.data.map.response.ResMapAddress
import kotlinx.coroutines.flow.Flow

internal interface MapRepository {
    fun getAddress(coords:String) : Flow<Result<ResMapAddress>>
}