package com.medicine.pedcalc.controllers.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class Pricing(
    val billable: Boolean,
    @JsonProperty("pricing_model") val pricingModel: String,
    val category: String
)
