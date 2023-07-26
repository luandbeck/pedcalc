package com.medicine.pedcalc.controllers.dtos

data class Entry(
    val id: String,
    val changes: List<Change>
)
