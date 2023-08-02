package com.medicine.pedcalc.domain.models.medicamentos

import com.medicine.pedcalc.domain.models.MedicamentoBase
import com.medicine.pedcalc.domain.utils.isValidDouble
import com.medicine.pedcalc.domain.utils.transformaEmMultiploInteiro
import kotlin.math.ceil

class Morfina : MedicamentoBase() {
    var peso: String? = null
    var dose: String? = null

    override fun calculaDose(): String {
        val pesoConvertido: Double = peso!!.toDouble()
        val doseConvertida: Double = dose!!.toDouble()

        val doseCalculada = pesoConvertido * doseConvertida

        return (this.calculoParaAmpolaUmMg(doseCalculada) ?: "") + this.calculoParaAmpolaDezMg(doseCalculada)
    }

    private fun calculoParaAmpolaUmMg(doseCalculada: Double): String? {
        val quantidadeMorfinaAmpola = ceil(doseCalculada).toInt()
        val quantidadeADDestilada = 10 - quantidadeMorfinaAmpola

        if (quantidadeMorfinaAmpola > 7) {
            return null
        }

        val doseFinal = (doseCalculada * 10) / quantidadeMorfinaAmpola
        val preTextoAmpola1 = "*Para Ampola de 1mg/ml*\n"
        val textoPrincipalAmpola1 = "Diluir $quantidadeMorfinaAmpola ampola(s) de $className em $quantidadeADDestilada${"ml"} de Água Destilada e fazer *$doseFinal${"ml"}* EV\n\n"

        return preTextoAmpola1 + textoPrincipalAmpola1
    }

    private fun calculoParaAmpolaDezMg(doseCalculada: Double): String {
        val doseCalculadaArredondada = doseCalculada.transformaEmMultiploInteiro(10)
        val quantidadeMorfinaAmpola = doseCalculadaArredondada / 10
        val solucaoTotal = doseCalculadaArredondada + 10

        val doseFinal = "%.2f".format((doseCalculada * solucaoTotal) / doseCalculadaArredondada)

        val preTextoAmpola10 = "*Para Ampola de 10mg/ml*\n"
        val textoPrincipalAmpola10 = "Diluir $quantidadeMorfinaAmpola ampola(s) de $className em 10ml de Água Destilada e fazer *$doseFinal${"ml"}* EV"
        return preTextoAmpola10 + textoPrincipalAmpola10
    }


    override fun retornaAjuda(): String {
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
