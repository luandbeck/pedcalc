package com.medicine.pedcalc.controllers

import com.medicine.pedcalc.controllers.dtos.WhatsAppBusinessAccountRequest
import com.medicine.pedcalc.domain.models.Solicitation
import com.medicine.pedcalc.domain.services.OrquestradorMedicamentoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/calcula-ped")
class CalculaMedicamentoController(
    val service: OrquestradorMedicamentoService
) {

    private val MY_TOKEN_NAME = "MY_TOKEN"

    @GetMapping("/webhook")
    fun webhook(
        @RequestParam(value = "hub.mode") mode: String,
        @RequestParam(value = "hub.challenge") challenge: String,
        @RequestParam(value = "hub.verify_token") token: String
    ): String {

        if (token == System.getenv(MY_TOKEN_NAME)) {
            println("Chamada Get")
            return challenge
        }

        throw RuntimeException()
    }

    @PostMapping("/webhook")
    fun postWebhook(@RequestBody body: WhatsAppBusinessAccountRequest): ResponseEntity<Unit> {
        if (body.entry.first().changes.first().value.messages?.isEmpty() != false) {
            print("Requisição ignorada")
            print("\n")
            print(body)
            print("\n")
            return ResponseEntity.status(HttpStatus.CREATED).build()
        }

        print("Requisição aceita")
        print("\n")
        print(body)
        print("\n")
        val solicitacao = this.buildSolicitation(body)
        this.service.execute(solicitacao)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("/teste")
    fun teste() {

        return this.service.teste()
    }

    private fun buildSolicitation(body: WhatsAppBusinessAccountRequest): Solicitation {
        val value = body.entry.first().changes.first().value
        val completeMessage = value.messages!!.first()

        val botPhone = value.metadata.phoneNumberId
        val clientPhone = completeMessage.from
        val textMessage = completeMessage.text.body

        print("Dados requisicao:$botPhone $clientPhone $textMessage")
        print("\n")

        return Solicitation(botPhone, clientPhone, textMessage)
    }
}