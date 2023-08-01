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
class OrquestradorMedicamentoService(
    private val calculateService: RespostaMedicamentoService,
    private val chat: OpenAIIntegration,
    private val whatsAppIntegration: WhatsAppIntegration
) {

    fun execute(message: CalcMessage) {
        return listOf(message)
            .map(this::helperResult)
            .map(this::calculateOriginalMessage)
            .map(this::calculateChatMessage)
            .map(this::generateErrorMessage)
            .map(this.whatsAppIntegration::sendMessage)
            .last()
    }

    fun helperResult(message: CalcMessage): CalcMessage {
        if (message.requestMessage.lowercase(Locale.getDefault()).contains("lista")) {
            message.response = ResponseMessage(buscaListaMedicamentos(), true)
        }

        return message
    }

    fun calculateOriginalMessage(message: CalcMessage): CalcMessage {
        if (message.response?.finalResult == true) {
            return message
        }

        val medicineMessage = parseMedicamentoFromString(message.requestMessage)
        if (medicineMessage != null) {
            message.response = calculateService.execute(medicineMessage)
        }

        return message
    }

    fun calculateChatMessage(message: CalcMessage): CalcMessage {
        if (message.response?.finalResult == true) {
            return message
        }

        val jsonChat = this.chat.callChat(message.requestMessage) ?: ""
        val medicineChat = parseMedicamentoFromJson(jsonChat)
        if (medicineChat != null) {
            message.response = calculateService.execute(medicineChat)
        }

        return message
    }

    fun generateErrorMessage(message: CalcMessage): CalcMessage {
        if (message.response != null) {
            return message
        }

        val errorMessage = "Não entendi o comando ou não encontrei o medicamento.\n" +
                "\n" +
                "Caso queira ver a lista de medicamentos disponíveis digite. 'lista medicamentos'.\n" +
                "\n" +
                "Caso queira ajuda sobre algum medicamento específico digite seu nome seguido de 'ajuda'. Exemplo: 'dipirona, ajuda'"

        message.response = ResponseMessage(errorMessage, true)
        return message
    }

    private fun buscaListaMedicamentos(): String {
        val classesMedicamento = buscaTodasClassesMedicamento()
        val nomesClasses = classesMedicamento.map { it.simpleName }

        return nomesClasses.joinToString("\n")
    }
}