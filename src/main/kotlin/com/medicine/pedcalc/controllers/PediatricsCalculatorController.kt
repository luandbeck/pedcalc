package com.medicine.pedcalc.controllers

import com.medicine.pedcalc.configurations.AppConfiguration
import com.medicine.pedcalc.controllers.dtos.WhatsAppBusinessAccountRequest
import com.medicine.pedcalc.domain.models.CalculationMessage
import com.medicine.pedcalc.domain.services.OrchestratorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.slf4j.MDCContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pedcalc")
class PediatricsCalculatorController(
    private val appConfiguration: AppConfiguration,
    private val service: OrchestratorService
) {

    private val log: Logger = LoggerFactory.getLogger(PediatricsCalculatorController::class.java)

    @GetMapping("/webhook")
    fun webhook(
        @RequestParam(value = "hub.mode") mode: String,
        @RequestParam(value = "hub.challenge") challenge: String,
        @RequestParam(value = "hub.verify_token") token: String
    ): String {
        log.info("chamda get")
        if (token == appConfiguration.myToken) {
            return challenge
        }

        throw RuntimeException()
    }

    @PostMapping("/webhook")
    fun postWebhook(@RequestBody body: WhatsAppBusinessAccountRequest): ResponseEntity<Unit> {
        if (body.entry.first().changes.first().value.messages?.isEmpty() != false) {
            log.info("Ignored request")
            log.info(body.toString())
            return ResponseEntity.status(HttpStatus.CREATED).build()
        }

        log.info("Accepted request")
        log.info(body.toString())
        val message = this.buildSolicitation(body)

        //Triggers the execution of the request in a separate thread by sending the traceId (MDC Context)
        CoroutineScope(Dispatchers.IO).launch(MDCContext()) {
            this@PediatricsCalculatorController.service.execute(message)
        }

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    private fun buildSolicitation(body: WhatsAppBusinessAccountRequest): CalculationMessage {
        val value = body.entry.first().changes.first().value
        val completeMessage = value.messages!!.first()

        val botPhone = value.metadata.phoneNumberId
        val clientPhone = completeMessage.from
        val textMessage = completeMessage.text.body

        log.info("Request data:$botPhone $clientPhone $textMessage")

        return CalculationMessage(botPhone, clientPhone, textMessage)
    }
}