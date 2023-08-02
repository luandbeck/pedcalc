package com.medicine.pedcalc.domain.services

import com.medicine.pedcalc.domain.models.Medication
import com.medicine.pedcalc.domain.models.ResponseMessage
import org.springframework.stereotype.Service

@Service
class MedicationResponseService {
    fun execute(medication: Medication): ResponseMessage = when {
        medication.help -> createResponse(medication.helpResponse())
        medication.isValid() -> createResponse(medication.calculates())
        else -> createResponse(medication.getMessageError(), false)
    }

    private fun createResponse(response: String, finalResponse: Boolean = true): ResponseMessage {
        return ResponseMessage(response, finalResponse)
    }
}