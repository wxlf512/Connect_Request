package dev.wxlf.connectrequest.data.remote.repositories

import dev.wxlf.connectrequest.data.models.HouseModel
import dev.wxlf.connectrequest.data.models.StreetModel
import dev.wxlf.connectrequest.data.remote.datasources.RemoteStatDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

internal class StatRepositoryImpl(
    private val remote: RemoteStatDataSource,
    private val externalScope: CoroutineScope
) : StatRepository {
    override suspend fun fetchAllStreets(): Result<ArrayList<StreetModel>> = kotlin.runCatching {
        externalScope.async {
            remote.loadAllStreets()
        }.await()
    }

    override suspend fun fetchHousesByStreetId(streetId: String): Result<ArrayList<HouseModel>> = kotlin.runCatching {
        externalScope.async {
            remote.loadHousesByStreetId(streetId)
        }.await()
    }

}