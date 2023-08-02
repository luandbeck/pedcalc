package com.medicine.pedcalc.domain.models.medicamentos

import com.medicine.pedcalc.domain.models.MedicamentoBase
import com.medicine.pedcalc.domain.utils.isValidDouble
import kotlin.math.round

class Tubo : MedicamentoBase() {
    var idade: String? = null
    var peso: String? = null

    override fun calculaDose(): String {
        val idadeConvertida: Int = round(idade!!.toDouble()).toInt()
        val pesoConvertido: Double? = converterStringParaDouble(peso)


        val tuboComCuff = trataValorQuebrado((idadeConvertida / 4.0) + 3.5)
        val tuboSemCuff = trataValorQuebrado((idadeConvertida / 4.0) + 4.0)

        return if (idadeConvertida > 0) {
            val fixacaoComCuff = tuboComCuff * 3
            val fixacaoSemCuff = tuboSemCuff * 3

            this.constroiResposta(tuboComCuff, tuboSemCuff, fixacaoComCuff, fixacaoSemCuff)

        } else {
            val fixacao = pesoConvertido!!.plus(6)

            this.constroiResposta(tuboComCuff, tuboSemCuff, fixacao, fixacao)
        }
    }

    private fun constroiResposta(
        tuboComCuff: Double,
        tuboSemCuff: Double,
        fixacaoComCuff: Double,
        fixacaoSemCuff: Double
    ): String {
        val preTextoComCuff = "*Com cuff:*\n"
        val textoPrincipalComCuff = "Utilizar $className de *$tuboComCuff${"mm"}* e fixação de *$fixacaoComCuff${"mm"}*\n\n"

        val preTextoSemCuff = "*Sem cuff:*\n"
        val textoPrincipalSemCuff = "Utilizar $className de *$tuboSemCuff${"mm"}* e fixação de *$fixacaoSemCuff${"mm"}*"

        return preTextoComCuff + textoPrincipalComCuff + preTextoSemCuff + textoPrincipalSemCuff
    }

    override fun retornaAjuda(): String {
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

    fun trataValorQuebrado(valor: Double): Double {
        val parteInteira = valor.toInt()
        val parteDecimal = valor - parteInteira

        return when {
            parteDecimal > 0.00 && parteDecimal < 0.50 -> parteInteira + 0.5
            parteDecimal > 0.50 -> parteInteira + 1.0
            else -> valor
        }
    }

    fun converterStringParaDouble(string: String?): Double? {
        return try {
            string?.toDouble()
        } catch (e: NumberFormatException) {
            null // Retorna null caso ocorra uma exceção
        }
    }

}
