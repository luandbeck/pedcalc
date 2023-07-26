package com.medicine.pedcalc.domain.utils

fun isValidDouble(str: String?): Boolean {
    return str?.toDoubleOrNull() != null
}