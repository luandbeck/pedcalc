package com.medicine.pedcalc.domain.models

import com.google.gson.Gson
import com.medicine.pedcalc.domain.utils.findAllMedications
import java.util.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

fun parseStringToMedication(input: String): Medication? {
    val medicationName = input.split(Regex("[,\\s]+"))[0].lowercase(Locale.getDefault())

    val emptyMedication = findAllMedications()
        .find { it.simpleName.lowercase(Locale.getDefault()) == medicationName }
        ?.getDeclaredConstructor()
        ?.newInstance() as? Medication

    emptyMedication?.let {
        return buildMedication(emptyMedication, input)
    }
        ?: return null

}

fun parseJsonToMedication(json: String?): Medication? {
    try {
        val gson = Gson()
        val jsonObject: Map<String, List<Map<String, String>>> =
            gson.fromJson(json, Map::class.java) as Map<String, List<Map<String, String>>>

        val className: String = jsonObject["medicamento"] as String? ?: return null
        val jsonProperties: List<Map<String, String>> = jsonObject["atributos"] ?: return null

        val stringBuilder = StringBuilder()
        stringBuilder.append(className)

        for (jsonProperty in jsonProperties) {
            val key = jsonProperty["nome"] ?: continue
            val value = jsonProperty["valor"] ?: continue

            stringBuilder.append(", $key $value")
        }

        return parseStringToMedication(stringBuilder.toString())
    } catch (e: Exception) {
        return null
    }
}

private fun buildMedication(medicationItem: Medication, input: String): Medication {
    val hasHelp = input.contains("ajuda", ignoreCase = true)
    if (hasHelp) {
        medicationItem.help = true

        return medicationItem
    }

    val medicationProperties = medicationItem::class.memberProperties
    val propertiesInput = input.substringAfter(',').trim().split(',')

    for (property in propertiesInput) {
        val key = property.trim().substringBefore(" ")
        val value = property.trim().substringAfter(" ")
        if (value.isNotEmpty()) {
            val propertyFound = medicationProperties.find { it.name.equals(key, ignoreCase = true) }
            if (propertyFound != null && propertyFound is KMutableProperty<*>) {
                propertyFound.setter.call(medicationItem, value)
            }
        }
    }

    return medicationItem
}