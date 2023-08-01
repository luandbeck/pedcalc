package com.medicine.pedcalc.domain.models

abstract class MedicamentoBase : Medicamento {
    override var ajuda: Boolean = false
    protected var className = this::class.simpleName
}

