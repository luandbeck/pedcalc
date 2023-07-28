package com.medicine.pedcalc.domain.models

import com.google.gson.Gson
import com.medicine.pedcalc.domain.utils.buscaTodasClassesMedicamento
import java.util.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

fun parseMedicamentoFromString(input: String): Medicamento? {
    val medicamentoName = input.split(Regex("[,\\s]+"))[0].lowercase(Locale.getDefault())

    val medicamentoClass = buscaTodasClassesMedicamento()
        .find { it.simpleName.lowercase(Locale.getDefault()) == medicamentoName }
        ?.getDeclaredConstructor()
        ?.newInstance() as? Medicamento

    medicamentoClass?.let {
        return criaMedicamento(medicamentoClass, input)
    }
        ?: return null

}

fun parseMedicamentoFromJson(json: String?): Medicamento? {
    try {
        val gson = Gson()
        val jsonObject: Map<String, List<Map<String, String>>> =
            gson.fromJson(json, Map::class.java) as Map<String, List<Map<String, String>>>

        val className: String = jsonObject["medicamento"] as String? ?: return null
        val atributosJson: List<Map<String, String>> = jsonObject["atributos"] ?: return null

        val stringBuilder = StringBuilder()
        stringBuilder.append(className)

        for (atributoJson in atributosJson) {
            val atributo = atributoJson["nome"] ?: continue
            val valor = atributoJson["valor"] ?: continue

            stringBuilder.append(", $atributo $valor")
        }

        return parseMedicamentoFromString(stringBuilder.toString())
    } catch (e: Exception) {
        return null
    }
}

private fun criaMedicamento(medicamentoClass: Medicamento, input: String): Medicamento {
    val contemAjuda = input.contains("ajuda", ignoreCase = true)
    if (contemAjuda) {
        medicamentoClass.ajuda = true

        return medicamentoClass
    }

    val atributosMedicamento = medicamentoClass::class.memberProperties
    val variaveis = input.substringAfter(',').trim().split(',')

    for (variavel in variaveis) {
        val chave = variavel.trim().substringBefore(" ")
        val valor = variavel.trim().substringAfter(" ")
        if (valor.isNotEmpty()) {
            val atributoEncontrado = atributosMedicamento.find { it.name.equals(chave, ignoreCase = true) }
            if (atributoEncontrado != null && atributoEncontrado is KMutableProperty<*>) {
                atributoEncontrado.setter.call(medicamentoClass, valor)
            }
        }
    }

    return medicamentoClass
}