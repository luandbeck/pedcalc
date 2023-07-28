package com.medicine.pedcalc.adapters.externals

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole.Companion.User
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.medicine.pedcalc.domain.ports.externals.OpenAIIntegration
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
@OptIn(BetaOpenAI::class)
class OpenAIIntegrationImpl(private val openAI: OpenAI) : OpenAIIntegration {
    private val log: Logger = LoggerFactory.getLogger(OpenAIIntegrationImpl::class.java)

    private val CHAT_MODEL = "gpt-3.5-turbo"

    override fun callChat(text: String): String? = runBlocking {
        val content =
            "Voce poderia converter o texto abaixo em um JSON de modo que ele tenha o nome do medicamento desejado e o valor das variaveis recebidas sem suas unidades ? São exemplos de variaveis: peso, dose, diametro, idade, temperatura, etc. \\n É IMPORTANTISSIMO que voce responda apenas o Json e mais nenhum texto, se você não encontrar nenhuma informação, pode responder o JSON em branco.  \\n Texto: '$text' \\n JSON Modelo: {\"medicamento\":\"nomeMedicamento\",\"atributos\":[{\"nome\":\"nomePropriedade1\",\"valor\":\"valorPropriedade1\"},{\"nome\":\"nomePropriedade2\",\"valor\":\"valorPropriedade2\"},{\"nome\":\"nomePropriedadeX\",\"valor\":\"valorPropriedadeX\"}]}"

        val message = ChatMessage(role = User, content = content)
        val request = ChatCompletionRequest(model = ModelId(CHAT_MODEL), messages = listOf(message), temperature = 0.2)

        val response = openAI.chatCompletion(request).choices.first().message?.content
        log.info("RESPOSTA CHAT: $response")
        response
    }
}