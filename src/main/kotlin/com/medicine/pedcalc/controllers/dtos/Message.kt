package com.medicine.pedcalc.controllers.dtos

data class Message(
    val from: String,
    val id: String,
    val timestamp: String,
    val text: Text,
    val type: String
)
