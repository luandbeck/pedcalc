package com.medicine.pedcalc.domain.models

interface Medicamento {
    var ajuda: Boolean
    fun calculaDose(): String
    fun retornaAjuda(): String
    fun isValid(): Boolean
    fun getMessageError(): String
    fun getStandardValue(): String
}