package com.medicine.pedcalc.domain.ports.externals

import org.springframework.stereotype.Service

@Service
interface OpenAIIntegration {

    fun callChat(text: String): String?
}