package com.medicine.pedcalc.controllers.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class Metadata(
    @JsonProperty("display_phone_number") val displayPhoneNumber: String,
    @JsonProperty("phone_number_id") val phoneNumberId: String
)
