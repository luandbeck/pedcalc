package com.medicine.pedcalc.controllers.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class Contact(
    val profile: Profile,
    @JsonProperty("wa_id") val waId: String
)