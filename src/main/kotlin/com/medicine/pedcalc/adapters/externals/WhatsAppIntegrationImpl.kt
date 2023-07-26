package com.medicine.pedcalc.adapters.externals

import com.medicine.pedcalc.adapters.externals.dtos.WhatsAppSendMessageRequest
import com.medicine.pedcalc.adapters.externals.dtos.WhatsAppText
import com.medicine.pedcalc.domain.models.Solicitation
import com.medicine.pedcalc.domain.ports.externals.WhatsAppIntegration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class WhatsAppIntegrationImpl(private val restTemplate: RestTemplate) : WhatsAppIntegration {

    private val TOKEN_NAME = "WHATSAPP_TOKEN"

    override fun sendMessage(solicitation: Solicitation, resposta: String) {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val token = System.getenv(TOKEN_NAME)
        val url = "https://graph.facebook.com/v17.0/${solicitation.telefoneBot}/messages?access_token=$token"
        val request = buildRequest(solicitation, resposta)
        restTemplate.exchange(
            url,
            HttpMethod.POST,
            HttpEntity(request, headers),
            String::class.java
        )
    }

    private fun buildRequest(solicitation: Solicitation, resposta: String): WhatsAppSendMessageRequest {
        return WhatsAppSendMessageRequest(
            "whatsapp", "individual", solicitation.telefoneCliente, "text", WhatsAppText(false, resposta)
        )
    }
}