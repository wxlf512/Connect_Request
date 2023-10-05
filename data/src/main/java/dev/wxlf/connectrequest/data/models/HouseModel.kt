package dev.wxlf.connectrequest.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class HouseModel(
    @JsonProperty("house_id")
    val houseId: String,
    @JsonProperty("house")
    val house: String
)