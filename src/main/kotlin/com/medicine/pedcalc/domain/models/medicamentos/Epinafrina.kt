package com.medicine.pedcalc.domain.models.medicamentos

import com.medicine.pedcalc.domain.models.MedicamentoBase
import com.medicine.pedcalc.domain.utils.isValidDouble

class Epinafrina : MedicamentoBase() {
    var peso: String? = null
    var dose: String? = "0.01"

    override fun calculaDose(): String {
        val pesoConvertido: Double = peso!!.toDouble()
        val doseConvertida: Double = dose!!.toDouble()

        val doseCalculada = pesoConvertido * doseConvertida
        val doseDiluida = doseCalculada * 10

        return this.constroiResposta(doseCalculada, doseDiluida)
    }

    private fun constroiResposta(doseCalculada: Double, doseDiluida: Double): String {
        val preTextoEV = "*EV na parada:* \n"
        val textoPrincipalEV = "Diluir 1ml de $className em 9ml de Água Destilada e dar *$doseDiluida${"ml"}*\n\n"

        val preTextoEndotraqual = "*Endotraquial na parada:* \n"
        val textoPrincipalEndotraquial = "Fazer *$doseCalculada${"ml"}* endotraquial *- NÃO DILUIR*\n"
        val posTextoEndotraquial = "Após administração, realizar 5 ventilações com ambú.\n\n"

        val preTextoAnafilaxia = "*Anafilaxia:* \n"
        val textoPrincipalAnafilaxia = "Fazer *$doseCalculada${"ml"}* intramuscular(IM)* - NÃO DILUIR*\n"

        return preTextoEV + textoPrincipalEV +
                preTextoEndotraqual + textoPrincipalEndotraquial + posTextoEndotraquial +
                preTextoAnafilaxia + textoPrincipalAnafilaxia
    }

    override fun retornaAjuda(): String {
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
