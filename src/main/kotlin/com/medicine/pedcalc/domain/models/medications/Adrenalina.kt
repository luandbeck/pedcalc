package com.medicine.pedcalc.domain.models.medications

import com.medicine.pedcalc.domain.models.MedicationBase
import com.medicine.pedcalc.domain.utils.isValidDouble

class Adrenalina : MedicationBase() {
    var peso: String? = null
    var dose: String? = "0.01"

    override fun calculates(): String {
        val convertedWeight: Double = peso!!.toDouble()
        val convertedDose: Double = dose!!.toDouble()

        val calculatedDose = convertedWeight * convertedDose
        val dilutedDose = calculatedDose * 10

        return this.buildResponse(calculatedDose, dilutedDose)
    }

    private fun buildResponse(calculatedDose: Double, dilutedDose: Double): String {
        val preTextEV = "*EV na parada:* \n"
        val principalTextEv = "Diluir 1ml de $className em 9ml de Água Destilada e dar *$dilutedDose${"ml"}*\n\n"

        val preTextEndotracheal = "*Endotraquial na parada:* \n"
        val principalTextEndotracheal = "Fazer *$calculatedDose${"ml"}* endotraquial *- NÃO DILUIR*\n"
        val posTextEndotracheal = "Após administração, realizar 5 ventilações com ambú.\n\n"

        val preTextAnaphylaxis = "*Anafilaxia:* \n"
        val principalTextAnaphylaxis = "Fazer *$calculatedDose${"ml"}* intramuscular(IM) *- NÃO DILUIR*\n"

        return preTextEV + principalTextEv +
                preTextEndotracheal + principalTextEndotracheal + posTextEndotracheal +
                preTextAnaphylaxis + principalTextAnaphylaxis
    }

    override fun helpResponse(): String {
        return "Para calcular a $className informe o peso do paciente em kg. Exemplo: '$className, peso 12.5'."
    }

    override fun getMessageError(): String {
        return "Algum parâmetro não foi enviado ou está no formato inválido. Garanta que esteja seguindo o exemplo '$className, peso 12.5."
    }

    override fun getStandardValue(): String {
        return "0.01"
    }

    override fun isValid(): Boolean {
        return listOf(this.peso, this.dose).all { param -> isValidDouble(param) }
    }

}
