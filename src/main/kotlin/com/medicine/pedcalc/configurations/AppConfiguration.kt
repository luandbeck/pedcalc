package com.medicine.pedcalc.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfiguration {

    @Value("\${application.variables.my-token}")
    lateinit var myToken: String

    @Value("\${application.variables.whatsapp-token}")
    lateinit var whatsAppToken: String

    @Value("\${application.variables.openai-token}")
    lateinit var openAIToken: String
}