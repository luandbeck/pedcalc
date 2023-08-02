package com.medicine.pedcalc.domain.models

interface Medication {
    var help: Boolean
    fun calculates(): String
    fun helpResponse(): String
    fun isValid(): Boolean
    fun getMessageError(): String
    fun getStandardValue(): String
}