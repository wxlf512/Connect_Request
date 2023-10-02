package dev.wxlf.connectrequest.data.remote.datasources

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
            statAPI.getAllStreets()
        }

    override suspend fun loadHousesByStreetId(streetId: String): ArrayList<HouseModel> =
        withContext(ioDispatcher) {
            statAPI.getHousesByStreetId(streetId)
        }
}