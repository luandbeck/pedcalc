package com.medicine.pedcalc.domain.services

import com.medicine.pedcalc.domain.models.CalcMessage
import com.medicine.pedcalc.domain.models.ResponseMessage
import com.medicine.pedcalc.domain.models.parseMedicamentoFromJson
import com.medicine.pedcalc.domain.models.parseMedicamentoFromString
import com.medicine.pedcalc.domain.ports.externals.OpenAIIntegration
import com.medicine.pedcalc.domain.ports.externals.WhatsAppIntegration
import com.medicine.pedcalc.domain.utils.buscaTodasClassesMedicamento
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrquestradorMedicamentoService2(
    private val calculateService: RespostaMedicamentoService,
    private val chat: OpenAIIntegration,
    private val whatsAppIntegration: WhatsAppIntegration
) {

    fun execute(message: CalcMessage): CalcMessage {
        when {
            helperResult(message) -> return message
            calculateOriginalMessage(message) -> return message
            calculateChatMessage(message) -> return message
            else -> generateErrorMessage(message)
        }
        return message
    }

    fun helperResult(message: CalcMessage): Boolean {
        if (message.requestMessage.lowercase(Locale.getDefault()).contains("lista")) {
            message.response = ResponseMessage(buscaListaMedicamentos(), true)
        }

        return message.response?.finalResult == true
    }

    fun calculateOriginalMessage(message: CalcMessage): Boolean {
        val medicineMessage = parseMedicamentoFromString(message.requestMessage)
        if (medicineMessage != null) {
            message.response = calculateService.execute(medicineMessage)
        }

        return message.response?.finalResult == true
    }

    fun calculateChatMessage(message: CalcMessage): Boolean {
        val jsonChat = this.chat.callChat(message.requestMessage) ?: ""
        val medicineChat = parseMedicamentoFromJson(jsonChat)
        if (medicineChat != null) {
            message.response = calculateService.execute(medicineChat)
        }

        return message.response?.finalResult == true
    }

    fun generateErrorMessage(message: CalcMessage) {
        // Implemente aqui o que deseja fazer no método final
    }

    private fun buscaListaMedicamentos(): String {
        val classesMedicamento = buscaTodasClassesMedicamento()
        val nomesClasses = classesMedicamento.map { it.simpleName }

        return nomesClasses.joinToString("\n")
    }
}