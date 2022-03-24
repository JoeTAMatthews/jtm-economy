package com.jtmnetwork.economy.entrypoint.controller

import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.model.TransactionType
import com.jtmnetwork.economy.data.service.TransactionService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(TransactionController::class)
@AutoConfigureWebTestClient
class TransactionControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var transactionService: TransactionService

    private val created = Transaction(UUID.randomUUID(), TransactionType.IN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 500.50, 50.50, 550.50, System.currentTimeMillis())

    @Test
    fun postTransaction() {
        `when`(transactionService.insertTransaction(anyOrNull())).thenReturn(Mono.just(created))

        testClient.post()
                .uri("/transaction")
                .bodyValue(created)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isEqualTo(created.id.toString())
                .jsonPath("$.type").isEqualTo(created.type.name)
                .jsonPath("$.sender").isEqualTo(created.sender.toString())
                .jsonPath("$.receiver").isEqualTo(created.receiver.toString())
                .jsonPath("$.currency").isEqualTo(created.currency.toString())
                .jsonPath("$.amount").isEqualTo(created.amount)
                .jsonPath("$.previous_balance").isEqualTo(created.previous_balance)
                .jsonPath("$.new_balance").isEqualTo(created.new_balance)

        verify(transactionService, times(1)).insertTransaction(anyOrNull())
        verifyNoMoreInteractions(transactionService)
    }

    @Test
    fun putTransaction() {
        `when`(transactionService.updateTransaction(anyOrNull())).thenReturn(Mono.just(created))

        testClient.put()
                .uri("/transaction")
                .bodyValue(created)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isEqualTo(created.id.toString())
                .jsonPath("$.type").isEqualTo(created.type.name)
                .jsonPath("$.sender").isEqualTo(created.sender.toString())
                .jsonPath("$.receiver").isEqualTo(created.receiver.toString())
                .jsonPath("$.currency").isEqualTo(created.currency.toString())
                .jsonPath("$.amount").isEqualTo(created.amount)
                .jsonPath("$.previous_balance").isEqualTo(created.previous_balance)
                .jsonPath("$.new_balance").isEqualTo(created.new_balance)

        verify(transactionService, times(1)).updateTransaction(anyOrNull())
        verifyNoMoreInteractions(transactionService)
    }

    @Test
    fun getTransaction() {
        `when`(transactionService.getTransaction(anyOrNull())).thenReturn(Mono.just(created))

        testClient.get()
                .uri("/transaction/${UUID.randomUUID()}")
                .exchange()
                .expectStatus().isOk
                    .expectBody()
                .jsonPath("$.id").isEqualTo(created.id.toString())
                .jsonPath("$.type").isEqualTo(created.type.name)
                .jsonPath("$.sender").isEqualTo(created.sender.toString())
                .jsonPath("$.receiver").isEqualTo(created.receiver.toString())
                .jsonPath("$.currency").isEqualTo(created.currency.toString())
                .jsonPath("$.amount").isEqualTo(created.amount)
                .jsonPath("$.previous_balance").isEqualTo(created.previous_balance)
                .jsonPath("$.new_balance").isEqualTo(created.new_balance)

        verify(transactionService, times(1)).getTransaction(anyOrNull())
        verifyNoMoreInteractions(transactionService)
    }

    @Test
    fun getTransactions() {
        `when`(transactionService.getTransactions()).thenReturn(Flux.just(created, Transaction(UUID.randomUUID(), TransactionType.OUT, null, UUID.randomUUID(), UUID.randomUUID(), 1000.0, 500.0, 1500.0, System.currentTimeMillis())))

        testClient.get()
                .uri("/transaction/all")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(created.id.toString())
                .jsonPath("$[0].type").isEqualTo(created.type.name)
                .jsonPath("$.sender").isEqualTo(created.sender.toString())
                .jsonPath("$.receiver").isEqualTo(created.receiver.toString())
                .jsonPath("$.currency").isEqualTo(created.currency.toString())
                .jsonPath("$.amount").isEqualTo(created.amount)
                .jsonPath("$.previous_balance").isEqualTo(created.previous_balance)
                .jsonPath("$.new_balance").isEqualTo(created.new_balance)

                .jsonPath("$[1].type").isEqualTo(TransactionType.OUT.name)
                .jsonPath("$[1].amount").isEqualTo(1000.0)
                .jsonPath("$[1].previous_balance").isEqualTo(500.0)
                .jsonPath("$[1].new_balance").isEqualTo(1500.0)

        verify(transactionService, times(1)).getTransactions()
        verifyNoMoreInteractions(transactionService)
    }

    @Test
    fun deleteTransaction() {
        `when`(transactionService.deleteTransaction(anyOrNull())).thenReturn(Mono.just(created))

        testClient.delete()
                .uri("/transaction/${UUID.randomUUID()}")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isEqualTo(created.id.toString())
                .jsonPath("$.type").isEqualTo(created.type.name)
                .jsonPath("$.sender").isEqualTo(created.sender.toString())
                .jsonPath("$.receiver").isEqualTo(created.receiver.toString())
                .jsonPath("$.currency").isEqualTo(created.currency.toString())
                .jsonPath("$.amount").isEqualTo(created.amount)
                .jsonPath("$.previous_balance").isEqualTo(created.previous_balance)
                .jsonPath("$.new_balance").isEqualTo(created.new_balance)

        verify(transactionService, times(1)).deleteTransaction(anyOrNull())
        verifyNoMoreInteractions(transactionService)
    }
}