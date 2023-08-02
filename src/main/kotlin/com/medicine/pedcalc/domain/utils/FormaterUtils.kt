package com.medicine.pedcalc.domain.utils

import kotlin.math.ceil

fun isValidDouble(str: String?): Boolean {
    return str?.toDoubleOrNull() != null
}

fun Double.convertToIntegerMultiple(multiple: Int): Int {
    val intValue = ceil(this).toInt()

    return if (intValue % multiple == 0) {
        intValue
    } else {
        (intValue / multiple + 1) * multiple
    }
}

fun convertStringToDouble(string: String?): Double? {
    return try {
        string?.toDouble()
    } catch (e: NumberFormatException) {
        null // Retorna null caso ocorra uma exceção
    }
}