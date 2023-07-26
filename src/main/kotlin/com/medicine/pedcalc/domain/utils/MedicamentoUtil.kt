package com.medicine.pedcalc.domain.utils

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AssignableTypeFilter


//fun buscaTodasClassesMedicamento(): List<Class<*>> {
//    val packagePrefix = "com.example.calculaped.domain.models.medicamentos"
//    val classLoader = Thread.currentThread().contextClassLoader
//    val resources = classLoader.getResources(packagePrefix.replace('.', '/'))
//
//    val classNames = mutableListOf<String>()
//
//    while (resources.hasMoreElements()) {
//        val resource = resources.nextElement()
//        val file = File(resource.toURI())
//        val files = file.listFiles() ?: continue
//
//        for (innerFile in files) {
//            if (innerFile.isFile && innerFile.name.endsWith(".class")) {
//                val className = innerFile.nameWithoutExtension
//                classNames.add("$packagePrefix.$className")
//            }
//        }
//    }
//
//    return classNames.mapNotNull {
//        try {
//            Class.forName(it)
//        } catch (e: ClassNotFoundException) {
//            null
//        }
//    }
//}

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