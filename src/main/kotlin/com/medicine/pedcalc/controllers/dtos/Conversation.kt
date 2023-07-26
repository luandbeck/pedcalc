package com.medicine.pedcalc.controllers.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class Conversation(
    val id: String,
    @JsonProperty("expiration_timestamp") val expirationTimestamp: String?,
    val origin: Origin
)