package com.medicine.pedcalc.domain.ports.externals

import com.medicine.pedcalc.domain.models.CalcMessage
import org.springframework.stereotype.Service

@Service
interface WhatsAppIntegration {

    fun sendMessage(calcMessage: CalcMessage)
}