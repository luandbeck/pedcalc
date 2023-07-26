package com.medicine.pedcalc.domain.models.medicamentos

import com.medicine.pedcalc.domain.models.MedicamentoBase
import com.medicine.pedcalc.domain.utils.isValidDouble

class Paracetamol : MedicamentoBase() {
    var peso: String? = null
    var dose: String? = null

    override fun calculaDose(): String {
        val pesoConvertido: Double = peso!!.toDouble()
        val doseConvertida: Double = dose!!.toDouble()

        val doseCalculada = pesoConvertido * doseConvertida

        return if (doseCalculada > 500) {
            val dosefinal = (doseCalculada * 10) / 1000

            "Diluir 2ml de Dipirona em 8ml de Água Destilada e dar $dosefinal${"ml"}"
        } else {
            val dosefinal = (doseCalculada * 10) / 500
            "Diluir 1ml de Dipirona em 9ml de Água Destilada e dar $dosefinal${"ml"}"
        }
    }

    override fun retornaAjuda(): String {
        return "Para calcular a Dipirona informe o peso do paciente em kg e a dose da medicação em mg. Exemplo: 'Dipirona, peso 12.5, dose 25'."
    }

    override fun getMessageError(): String {
        return "Algum parâmetro não foi enviado ou está no formato inválido. Garanta que esteja seguindo o exemplo 'Dipirona, peso 12.5, dose 25'."
    }

    override fun isValid(): Boolean {
        return listOf(this.peso, this.dose).all { param -> isValidDouble(param) }
    }

}
