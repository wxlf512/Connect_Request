package dev.wxlf.connectrequest.data.remote.datasources

import dev.wxlf.connectrequest.data.models.HouseModel
import dev.wxlf.connectrequest.data.models.StreetModel

interface RemoteStatDataSource {

    suspend fun loadAllStreets(): ArrayList<StreetModel>
    suspend fun loadHousesByStreetId(streetId: String): ArrayList<HouseModel>
}