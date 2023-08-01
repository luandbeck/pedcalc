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
class OrquestradorMedicamentoService1(
    private val calculateService: RespostaMedicamentoService,
    private val chat: OpenAIIntegration,
    private val whatsAppIntegration: WhatsAppIntegration
) {

    fun execute(message: CalcMessage) {
        val finalMessage = this.createResponse(message)
        whatsAppIntegration.sendMessage(message)
    }

    fun createResponse(message: CalcMessage): CalcMessage {
        val requestMessage = message.requestMessage

        if (requestMessage.lowercase(Locale.getDefault()).contains("lista")) {
            message.response = ResponseMessage(buscaListaMedicamentos(), true)
            return message
        }

        val medicineMessage = parseMedicamentoFromString(requestMessage)
        if (medicineMessage != null) {
            message.response = calculateService.execute(medicineMessage)
        }

        if (message.response?.finalResult != true) {
            val jsonChat = this.chat.callChat(requestMessage) ?: ""
            val medicineChat = parseMedicamentoFromJson(jsonChat)
            if (medicineChat != null) {
                message.response = calculateService.execute(medicineChat)
            }
        }

        if (message.response?.finalResult != true) {
            val errorMessage = "Não entendi o comando ou não encontrei o medicamento.\n" +
                    "\n" +
                    "Caso queira ver a lista de medicamentos disponíveis digite. 'lista medicamentos'.\n" +
                    "\n" +
                    "Caso queira ajuda sobre algum medicamento específico digite seu nome seguido de 'ajuda'. Exemplo: 'dipirona, ajuda'"

            message.response = ResponseMessage(errorMessage, true)
        }

        return message
    }

    private fun buscaListaMedicamentos(): String {
        val classesMedicamento = buscaTodasClassesMedicamento()
        val nomesClasses = classesMedicamento.map { it.simpleName }

        return nomesClasses.joinToString("\n")
    }
}