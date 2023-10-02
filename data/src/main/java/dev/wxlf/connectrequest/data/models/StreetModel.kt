package dev.wxlf.connectrequest.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class StreetModel(
    @JsonProperty("street")
    val street: String,
    @JsonProperty("street_id")
    val streetId: String
)