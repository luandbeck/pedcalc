package com.medicine.pedcalc.domain.models

data class CalculationMessage(
    val botPhone: String,
    val clientPhone: String,
    val requestMessage: String,
    var response: ResponseMessage? = null
)
