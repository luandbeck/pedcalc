package com.medicine.pedcalc.controllers.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class Status(
    val id: String,
    val status: String,
    val timestamp: String,
    @JsonProperty("recipient_id") val recipientId: String,
    val conversation: Conversation,
    val pricing: Pricing
)