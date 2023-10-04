package dev.wxlf.connectrequest.data.remote.datasources

import android.util.Log
import dev.wxlf.connectrequest.data.models.HouseModel
import dev.wxlf.connectrequest.data.models.StreetModel
import dev.wxlf.connectrequest.data.remote.StatAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class RetrofitStatDataSource(
    private val statAPI: StatAPI,
    private val ioDispatcher: CoroutineDispatcher
) : RemoteStatDataSource {
    override suspend fun loadAllStreets(): ArrayList<StreetModel> =
        withContext(ioDispatcher) {
            Log.d("RetrofitStatDataSource", "Streets request")
            statAPI.getAllStreets()
        }

    override suspend fun loadHousesByStreetId(streetId: String): ArrayList<HouseModel> =
        withContext(ioDispatcher) {
            Log.e("RetrofitStatDataSource", "Houses request for street $streetId")
            statAPI.getHousesByStreetId(streetId)
        }
}