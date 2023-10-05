package dev.wxlf.connectrequest.request_ui.util

import dev.wxlf.connectrequest.data.models.HouseModel
import dev.wxlf.connectrequest.data.models.StreetModel
import dev.wxlf.connectrequest.request_ui.models.HouseDisplayableModel
import dev.wxlf.connectrequest.request_ui.models.StreetDisplayableModel

@JvmName("streetsMapper")
fun ArrayList<StreetModel>.mapToDisplayable(): List<StreetDisplayableModel> =
    map { StreetDisplayableModel(streetId = it.streetId, street = it.street) }

@JvmName("housesMapper")
fun ArrayList<HouseModel>.mapToDisplayable(): List<HouseDisplayableModel> =
    map { HouseDisplayableModel(houseId = it.houseId, house = it.house) }