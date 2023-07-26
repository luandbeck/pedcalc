package com.medicine.pedcalc.domain.models

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