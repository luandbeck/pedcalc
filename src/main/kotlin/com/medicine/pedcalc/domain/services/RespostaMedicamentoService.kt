package com.medicine.pedcalc.domain.services

import com.medicine.pedcalc.domain.models.Medicamento
import com.medicine.pedcalc.domain.models.ResponseMessage
import org.springframework.stereotype.Service

@Service
class RespostaMedicamentoService {
    fun execute(medicamento: Medicamento): ResponseMessage = when {
        medicamento.ajuda -> createResponse(medicamento.retornaAjuda())
        medicamento.isValid() -> createResponse(medicamento.calculaDose())
        else -> createResponse(medicamento.getMessageError(), false)
    }

    private fun createResponse(response: String, finalResponse: Boolean = true): ResponseMessage {
        return ResponseMessage(response, finalResponse)
    }
}