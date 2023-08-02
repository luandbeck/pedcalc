package com.medicine.pedcalc.domain.models.medications

import com.medicine.pedcalc.domain.models.MedicationBase
import com.medicine.pedcalc.domain.utils.isValidDouble

class Cetamina : MedicationBase() {
    var peso: String? = null
    var dose: String? = null

    override fun calculates(): String {
        val convertedWeight: Double = peso!!.toDouble()
        val convertedDose: Double = dose!!.toDouble()

        val calculatedDose = convertedWeight * convertedDose

        val preText = "*Para sedação:* \n"
        val principalText = this.principalText(calculatedDose)
        val posText = "\n\n*Para sedação contínua:* A FAZER"

        return preText + principalText + posText
    }

    private fun principalText(calculatedDose: Double): String {
        return if (calculatedDose > 50) {
            val finalDose = (calculatedDose * 10) / 100

            "Diluir 2ml de $className em 8ml de Água Destilada e dar *$finalDose${"ml"}*"
        } else {
            val finalDose = (calculatedDose * 10) / 50
            "Diluir 1ml de $className em 9ml de Água Destilada e dar *$finalDose${"ml"}*"
        }
    }

    override fun helpResponse(): String {
        return "Para calcular a $className informe o peso do paciente em kg e a dose da medicação em mg. Exemplo: '$className, peso 12.5, dose ${getStandardValue()}'."
    }

    override fun getMessageError(): String {
        return "Algum parâmetro não foi enviado ou está no formato inválido. Garanta que esteja seguindo o exemplo '$className, peso 12.5, dose ${getStandardValue()}'."
    }

    override fun getStandardValue(): String {
        return "2"
    }

    override fun isValid(): Boolean {
        return listOf(this.peso, this.dose).all { param -> isValidDouble(param) }
    }

}
