package com.medicine.pedcalc.domain.ports.externals

import com.medicine.pedcalc.domain.models.CalculationMessage
import org.springframework.stereotype.Service

@Service
interface WhatsAppIntegration {

    fun sendMessage(calculationMessage: CalculationMessage)
}