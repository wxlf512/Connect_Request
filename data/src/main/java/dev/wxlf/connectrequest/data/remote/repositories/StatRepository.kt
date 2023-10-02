package dev.wxlf.connectrequest.data.remote.repositories

import dev.wxlf.connectrequest.data.models.HouseModel
import dev.wxlf.connectrequest.data.models.StreetModel

interface StatRepository {

    suspend fun fetchAllStreets(): Result<ArrayList<StreetModel>>
    suspend fun fetchHousesByStreetId(streetId: String): Result<ArrayList<HouseModel>>
}