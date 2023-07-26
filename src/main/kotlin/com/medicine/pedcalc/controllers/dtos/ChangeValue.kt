package com.medicine.pedcalc.controllers.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class ChangeValue(
    @JsonProperty("messaging_product") val messagingProduct: String,
    val metadata: Metadata,
    val contacts: List<Contact>? = null,
    val messages: List<Message>? = null,
)
