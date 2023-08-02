package com.medicine.pedcalc.domain.models.medications

import com.medicine.pedcalc.domain.models.MedicationBase
import com.medicine.pedcalc.domain.utils.convertStringToDouble
import com.medicine.pedcalc.domain.utils.isValidDouble
import kotlin.math.round

class Tubo : MedicationBase() {
    var idade: String? = null
    var peso: String? = null

    override fun calculates(): String {
        val convertedAge: Int = round(idade!!.toDouble()).toInt()
        val convertedWeight: Double? = convertStringToDouble(peso)


        val tubeWithCuff = roundToValidTubeDiameter((convertedAge / 4.0) + 3.5)
        val tubeWithoutCuff = roundToValidTubeDiameter((convertedAge / 4.0) + 4.0)

        return if (convertedAge > 0) {
            val fixationWithCuff = tubeWithCuff * 3
            val fixationWithoutCuff = tubeWithoutCuff * 3

            this.buildResponse(tubeWithCuff, tubeWithoutCuff, fixationWithCuff, fixationWithoutCuff)

        } else {
            val fixation = convertedWeight!!.plus(6)

            this.buildResponse(tubeWithCuff, tubeWithoutCuff, fixation, fixation)
        }
    }

    private fun buildResponse(
        tubeWithCuff: Double,
        tubeWithoutCuff: Double,
        fixationWithCuff: Double,
        fixationWithoutCuff: Double
    ): String {
        val preTextWithCuff = "*Com cuff:*\n"
        val principalTextWithCuff = "Utilizar $className de *$tubeWithCuff${"mm"}* e fixação de *$fixationWithCuff${"mm"}*\n\n"

        val preTextWithoutCuff = "*Sem cuff:*\n"
        val principalTextWithoutCuff = "Utilizar $className de *$tubeWithoutCuff${"mm"}* e fixação de *$fixationWithoutCuff${"mm"}*"

        return preTextWithCuff + principalTextWithCuff + preTextWithoutCuff + principalTextWithoutCuff
    }

    override fun helpResponse(): String {
        return "Para calcular o $className informe a idade do paciente. Para menores de 1 ano também é necessário o peso.\n\n" +
                "*Exemplo:* '$className, idade 1' ou '$className, idade 0, peso 6'"
    }

    override fun getMessageError(): String {
        return "Algum parâmetro não foi enviado ou está no formato inválido. Garanta que esteja enviando tudo corretamente.\n\n" +
                "*Exemplo:* '$className, idade 1' ou '$className, idade 0, peso 6'."
    }

    override fun isValid(): Boolean {
        if (!isValidDouble(this.idade)) {
            return false
        }

        if (idade!!.toInt() == 0) {
            return isValidDouble(peso)
        }

        return true
    }

    override fun getStandardValue(): String {
        return ""
    }

    private fun roundToValidTubeDiameter(value: Double): Double {
        val integerValue = value.toInt()
        val decimalValue = value - integerValue

        return when {
            decimalValue > 0.00 && decimalValue < 0.50 -> integerValue + 0.5
            decimalValue > 0.50 -> integerValue + 1.0
            else -> value
        }
    }

}
