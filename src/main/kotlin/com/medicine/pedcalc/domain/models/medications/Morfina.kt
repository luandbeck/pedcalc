package com.medicine.pedcalc.domain.models.medications

import com.medicine.pedcalc.domain.models.MedicationBase
import com.medicine.pedcalc.domain.utils.convertToIntegerMultiple
import com.medicine.pedcalc.domain.utils.isValidDouble
import kotlin.math.ceil

class Morfina : MedicationBase() {
    var peso: String? = null
    var dose: String? = null

    override fun calculates(): String {
        val convertedWeight: Double = peso!!.toDouble()
        val convertedDose: Double = dose!!.toDouble()

        val calculatedDose = convertedWeight * convertedDose

        return (this.calculationForAmpouleOneMg(calculatedDose) ?: "") + this.calculationForAmpouleTenMg(calculatedDose)
    }

    private fun calculationForAmpouleOneMg(calculatedDose: Double): String? {
        val quantityAmpoules = ceil(calculatedDose).toInt()
        val quantityDistilledWater = 10 - quantityAmpoules

        if (quantityAmpoules > 7) {
            return null
        }

        val finalDose = (calculatedDose * 10) / quantityAmpoules
        val preTextAmpouleOne = "*Para Ampola de 1mg/ml:*\n"
        val principalTextAmpouleOne = "Diluir $quantityAmpoules ampola(s) de $className em $quantityDistilledWater${"ml"} de Água Destilada e fazer *$finalDose${"ml"}* EV\n\n"

        return preTextAmpouleOne + principalTextAmpouleOne
    }

    private fun calculationForAmpouleTenMg(calculatedDose: Double): String {
        val calculatedRoundedDose = calculatedDose.convertToIntegerMultiple(10)
        val quantityAmpoule = calculatedRoundedDose / 10
        val finalSolution = calculatedRoundedDose + 10

        val finalDose = "%.2f".format((calculatedDose * finalSolution) / calculatedRoundedDose)

        val preTextAmpouleTen = "*Para Ampola de 10mg/ml:*\n"
        val principalTextAmpouleTen = "Diluir $quantityAmpoule ampola(s) de $className em 10ml de Água Destilada e fazer *$finalDose${"ml"}* EV"
        return preTextAmpouleTen + principalTextAmpouleTen
    }


    override fun helpResponse(): String {
        return "Para calcular a $className informe o peso do paciente em kg e a dose da medicação em mg. Exemplo: '$className, peso 12.5, dose ${getStandardValue()}'."
    }

    override fun getMessageError(): String {
        return "Algum parâmetro não foi enviado ou está no formato inválido. Garanta que esteja seguindo o exemplo '$className, peso 12.5, dose ${getStandardValue()}'."
    }

    override fun getStandardValue(): String {
        return "0.05"
    }

    override fun isValid(): Boolean {
        return listOf(this.peso, this.dose).all { param -> isValidDouble(param) }
    }

}
