package com.medicine.pedcalc.controllers.dtos

data class WhatsAppBusinessAccountRequest(
    val `object`: String,
    val entry: List<Entry>
)
