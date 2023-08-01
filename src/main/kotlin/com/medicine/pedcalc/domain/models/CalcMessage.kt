package com.medicine.pedcalc.domain.models

data class CalcMessage(
    val botPhone: String,
    val clientPhone: String,
    val requestMessage: String,
    var response: ResponseMessage? = null
)
