package com.medicine.pedcalc.adapters.externals.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class WhatsAppSendMessageRequest(
    @JsonProperty("messaging_product")
    val messagingProduct: String,
    @JsonProperty("recipient_type")
    val recipientType: String,
    val to: String,
    val type: String,
    val text: WhatsAppText
)
