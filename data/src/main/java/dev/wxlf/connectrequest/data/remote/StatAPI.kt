package dev.wxlf.connectrequest.data.remote

import dev.wxlf.connectrequest.data.models.HouseModel
import dev.wxlf.connectrequest.data.models.StreetModel
import retrofit2.http.GET
import retrofit2.http.Query

interface StatAPI {

    @GET("get/allStreets")
    suspend fun getAllStreets(): ArrayList<StreetModel>

    @GET("get/houses")
    suspend fun getHousesByStreetId(@Query("street_id") streetId: String): ArrayList<HouseModel>
}