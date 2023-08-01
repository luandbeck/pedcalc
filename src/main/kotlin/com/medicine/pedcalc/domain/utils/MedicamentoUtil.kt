package com.medicine.pedcalc.domain.utils

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AssignableTypeFilter

fun buscaTodasClassesMedicamento(): List<Class<*>> {
    val packagePrefix = "com.medicine.pedcalc.domain.models.medicamentos"

    val provider = ClassPathScanningCandidateComponentProvider(false)
    provider.addIncludeFilter(AssignableTypeFilter(Any::class.java))

    val classNames = mutableListOf<String>()
    val resources = provider.findCandidateComponents(packagePrefix)

    for (resource in resources) {
        val className = resource.beanClassName!!
        classNames.add(className)
    }

    return classNames.mapNotNull {
        try {
            Class.forName(it)
        } catch (e: ClassNotFoundException) {
            null
        }
    }
}