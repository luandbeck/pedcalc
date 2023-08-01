package com.medicine.pedcalc.adapters.externals

import com.medicine.pedcalc.adapters.externals.dtos.WhatsAppSendMessageRequest
import com.medicine.pedcalc.adapters.externals.dtos.WhatsAppText
import com.medicine.pedcalc.configurations.AppConfiguration
import com.medicine.pedcalc.domain.models.CalcMessage
import com.medicine.pedcalc.domain.ports.externals.WhatsAppIntegration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class WhatsAppIntegrationImpl(
    private val appConfiguration: AppConfiguration,
    private val restTemplate: RestTemplate
) : WhatsAppIntegration {

    override fun sendMessage(calcMessage: CalcMessage) {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val token = appConfiguration.whatsAppToken
        val url = "https://graph.facebook.com/v17.0/${calcMessage.botPhone}/messages?access_token=$token"
        val request = buildRequest(calcMessage)
        restTemplate.exchange(
            url,
            HttpMethod.POST,
            HttpEntity(request, headers),
            String::class.java
        )
    }

    private fun buildRequest(calcMessage: CalcMessage): WhatsAppSendMessageRequest {
        return WhatsAppSendMessageRequest(
            "whatsapp",
            "individual",
            calcMessage.clientPhone,
            "text",
            WhatsAppText(false, calcMessage.response!!.responseText)
        )
    }
}