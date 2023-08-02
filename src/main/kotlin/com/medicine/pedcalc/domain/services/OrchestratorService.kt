package com.medicine.pedcalc.domain.services

import com.medicine.pedcalc.domain.models.CalculationMessage
import com.medicine.pedcalc.domain.models.ResponseMessage
import com.medicine.pedcalc.domain.models.parseJsonToMedication
import com.medicine.pedcalc.domain.models.parseStringToMedication
import com.medicine.pedcalc.domain.ports.externals.OpenAIIntegration
import com.medicine.pedcalc.domain.ports.externals.WhatsAppIntegration
import com.medicine.pedcalc.domain.utils.findAllMedications
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrchestratorService(
    private val calculateService: MedicationResponseService,
    private val chat: OpenAIIntegration,
    private val whatsAppIntegration: WhatsAppIntegration
) {

    fun execute(message: CalculationMessage) {
        return listOf(message)
            .map(this::helperResult)
            .map(this::calculateOriginalMessage)
            .map(this::calculateChatMessage)
            .map(this::generateErrorMessage)
            .map(this.whatsAppIntegration::sendMessage)
            .last()
    }

    fun helperResult(message: CalculationMessage): CalculationMessage {
        if (message.requestMessage.lowercase(Locale.getDefault()).contains("lista")) {
            message.response = ResponseMessage(buildResponseAllMedications(), true)
        }

        return message
    }

    fun calculateOriginalMessage(message: CalculationMessage): CalculationMessage {
        if (message.response?.finalResult == true) {
            return message
        }

        val medicationMessage = parseStringToMedication(message.requestMessage)
        if (medicationMessage != null) {
            message.response = calculateService.execute(medicationMessage)
        }

        return message
    }

    fun calculateChatMessage(message: CalculationMessage): CalculationMessage {
        if (message.response?.finalResult == true) {
            return message
        }

        val jsonChat = this.chat.callChat(message.requestMessage) ?: ""
        val medicationChat = parseJsonToMedication(jsonChat)
        if (medicationChat != null) {
            message.response = calculateService.execute(medicationChat)
        }

        return message
    }

    fun generateErrorMessage(message: CalculationMessage): CalculationMessage {
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

    private fun buildResponseAllMedications(): String {
        val medicationClasses = findAllMedications()
        val classesNames = medicationClasses.map { it.simpleName }

        return classesNames.joinToString("\n")
    }
}