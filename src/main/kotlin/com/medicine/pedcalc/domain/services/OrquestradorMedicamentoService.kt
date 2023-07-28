package com.medicine.pedcalc.domain.services

import com.medicine.pedcalc.domain.models.Solicitation
import com.medicine.pedcalc.domain.models.parseMedicamentoFromJson
import com.medicine.pedcalc.domain.ports.externals.OpenAIIntegration
import com.medicine.pedcalc.domain.ports.externals.WhatsAppIntegration
import com.medicine.pedcalc.domain.utils.buscaTodasClassesMedicamento
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrquestradorMedicamentoService(
    private val calculoMedicamentoService: RespostaMedicamentoService,
    private val chat: OpenAIIntegration,
    private val whatsAppIntegration: WhatsAppIntegration
) {

    fun execute(solicitation: Solicitation) {
        val response = this.createResponse(solicitation.mensagem)
        whatsAppIntegration.sendMessage(solicitation, response)
    }

    fun createResponse(mensagem: String): String {
        if (mensagem.lowercase(Locale.getDefault()).contains("lista")) {
            return buscaListaMedicamentos()
        }

//        val medicineMessage = parseMedicamentoFromString(mensagem)
//        if (medicineMessage != null) {
//            return calculoMedicamentoService.execute(medicineMessage)
//        }

        val medicineChat = parseMedicamentoFromJson(this.chat.callChat(mensagem) ?: "")
        if (medicineChat != null) {
            return calculoMedicamentoService.execute(medicineChat)
        }

        return "Não entendi o comando ou não encontrei o medicamento.\n" +
                "\n" +
                "Caso queira ver a lista de medicamentos disponíveis digite. 'lista medicamentos'.\n" +
                "\n" +
                "Caso queira ajuda sobre algum medicamento específico digite seu nome seguido de 'ajuda'. Exemplo: 'dipirona, ajuda'"
    }

    private fun buscaListaMedicamentos(): String {
        val classesMedicamento = buscaTodasClassesMedicamento()
        val nomesClasses = classesMedicamento.map { it.simpleName }

        return nomesClasses.joinToString("\n")
    }
}