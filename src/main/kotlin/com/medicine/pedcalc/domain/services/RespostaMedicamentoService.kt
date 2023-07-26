package com.medicine.pedcalc.domain.services

import com.medicine.pedcalc.domain.models.Medicamento
import org.springframework.stereotype.Service

@Service
class RespostaMedicamentoService {
    fun execute(medicamento: Medicamento): String = when {
        medicamento.ajuda -> medicamento.retornaAjuda()
        medicamento.isValid() -> medicamento.calculaDose()
        else -> medicamento.getMessageError()
    }
}