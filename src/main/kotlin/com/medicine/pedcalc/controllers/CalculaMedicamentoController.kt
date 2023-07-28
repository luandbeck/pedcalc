package com.medicine.pedcalc.controllers

import com.medicine.pedcalc.configurations.AppConfiguration
import com.medicine.pedcalc.controllers.dtos.WhatsAppBusinessAccountRequest
import com.medicine.pedcalc.domain.models.Solicitation
import com.medicine.pedcalc.domain.services.OrquestradorMedicamentoService
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
@RequestMapping("/calcula-ped")
class CalculaMedicamentoController(
    private val appConfiguration: AppConfiguration,
    private val service: OrquestradorMedicamentoService
) {

    private val log: Logger = LoggerFactory.getLogger(CalculaMedicamentoController::class.java)

    private val MY_TOKEN_NAME = "MY_TOKEN"

    @GetMapping("/webhook")
    fun webhook(
        @RequestParam(value = "hub.mode") mode: String,
        @RequestParam(value = "hub.challenge") challenge: String,
        @RequestParam(value = "hub.verify_token") token: String
    ): String {
        if (token == appConfiguration.myToken) {
            log.info("Chamada Get")
            return challenge
        }

        throw RuntimeException()
    }

    @PostMapping("/webhook")
    fun postWebhook(@RequestBody body: WhatsAppBusinessAccountRequest): ResponseEntity<Unit> {
        if (body.entry.first().changes.first().value.messages?.isEmpty() != false) {
            log.info("Requisição ignorada")
            log.info(body.toString())
            return ResponseEntity.status(HttpStatus.CREATED).build()
        }

        log.info("Requisição aceita")
        log.info(body.toString())
        val solicitacao = this.buildSolicitation(body)

        //Dispara a execução da solicitação em uma thread separada enviando o traceId (Contexto MDC)
        CoroutineScope(Dispatchers.IO).launch(MDCContext()) {
            this@CalculaMedicamentoController.service.execute(solicitacao)
        }

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    private fun buildSolicitation(body: WhatsAppBusinessAccountRequest): Solicitation {
        val value = body.entry.first().changes.first().value
        val completeMessage = value.messages!!.first()

        val botPhone = value.metadata.phoneNumberId
        val clientPhone = completeMessage.from
        val textMessage = completeMessage.text.body

        log.info("Dados requisicao:$botPhone $clientPhone $textMessage")

        return Solicitation(botPhone, clientPhone, textMessage)
    }
}