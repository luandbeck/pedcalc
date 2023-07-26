package com.medicine.pedcalc.domain.ports.externals

import com.medicine.pedcalc.domain.models.Solicitation
import org.springframework.stereotype.Service

@Service
interface WhatsAppIntegration {

    fun sendMessage(solicitation: Solicitation, resposta: String)
}