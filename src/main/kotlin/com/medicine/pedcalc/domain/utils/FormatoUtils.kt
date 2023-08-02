package com.medicine.pedcalc.domain.utils

import kotlin.math.ceil

fun isValidDouble(str: String?): Boolean {
    return str?.toDoubleOrNull() != null
}

fun Double.transformaEmMultiploInteiro(multiplo: Int): Int {
    val valorInteiro = ceil(this).toInt()

    return if (valorInteiro % multiplo == 0) {
        valorInteiro
    } else {
        (valorInteiro / multiplo + 1) * multiplo
    }
}