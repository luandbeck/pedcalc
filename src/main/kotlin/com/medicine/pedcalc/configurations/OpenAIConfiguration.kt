package com.medicine.pedcalc.configurations

import com.aallam.openai.client.OpenAI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAIConfiguration(private val appConfiguration: AppConfiguration) {

    @Bean
    fun openAI(): OpenAI {
        return OpenAI(appConfiguration.openAIToken)
    }
}