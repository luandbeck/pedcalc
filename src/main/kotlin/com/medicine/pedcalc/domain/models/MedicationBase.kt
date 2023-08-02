package com.medicine.pedcalc.domain.models

abstract class MedicationBase : Medication {
    override var help: Boolean = false
    protected var className = this::class.simpleName
}

