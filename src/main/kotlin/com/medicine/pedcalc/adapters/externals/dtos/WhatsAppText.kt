package com.medicine.pedcalc.adapters.externals.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class WhatsAppText(
    @JsonProperty("preview_url")
    val previewUrl: Boolean,
    val body: String
)
