package com.medicine.pedcalc.domain.services

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole.Companion.User
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.medicine.pedcalc.domain.models.Solicitation
import com.medicine.pedcalc.domain.models.parseMedicamentoFromString
import com.medicine.pedcalc.domain.ports.externals.WhatsAppIntegration
import com.medicine.pedcalc.domain.utils.buscaTodasClassesMedicamento
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrquestradorMedicamentoService(
    private val calculoMedicamentoService: RespostaMedicamentoService,
    private val whatsAppIntegration: WhatsAppIntegration
) {

    fun execute(solicitation: Solicitation) {
        val response = this.createResponse(solicitation)
        whatsAppIntegration.sendMessage(solicitation, response)
    }

    private fun createResponse(solicitation: Solicitation): String {
        val mensagem = solicitation.mensagem

        if (mensagem.lowercase(Locale.getDefault()).contains("lista")) {
            return buscaListaMedicamentos()
        }

        val medicamento = parseMedicamentoFromString(mensagem)
        medicamento?.let {
            return calculoMedicamentoService.execute(medicamento)
        }

        return "Não entendi o comando ou não encontrei o medicamento.\n\nCaso queira ver a lista de medicamentos disponíveis digite. 'lista medicamentos'.\n\nCaso queira ajuda sobre algum medicamento específico digite seu nome seguido de 'ajuda'. Exemplo: 'dipirona, ajuda'"
    }

    private fun buscaListaMedicamentos(): String {
        val classesMedicamento = buscaTodasClassesMedicamento()
        val nomesClasses = classesMedicamento.map { it.simpleName }

        return nomesClasses.joinToString("\n")
    }

    @OptIn(BetaOpenAI::class)
    fun teste() = runBlocking {
        val openAI = OpenAI("das")

        val message = ChatMessage(role = User, content = "Hello, how are you?")
        val request1 = ChatCompletionRequest(model = ModelId("gpt-3.5-turbo-0613"), messages = listOf(message))
        print(openAI.chatCompletion(request1).choices.first().message)

//        val request =
//            CompletionRequest(model = ModelId("gpt-3.5-turbo-0613"), prompt = "This is a test", maxTokens = 4096)
//
//        print(openAI.completion(request).choices.first().text)
    }
}