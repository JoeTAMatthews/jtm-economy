package com.jtmnetwork.economy.entrypoint.controller

import com.jtmnetwork.economy.core.domain.entity.ExchangeRate
import com.jtmnetwork.economy.data.service.RateService
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
@WebFluxTest(RateController::class)
@AutoConfigureWebTestClient
class RateControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var rateService: RateService

    private val created = ExchangeRate(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "GBPEUR", 1.13)

    @Test
    fun postRate() {
        `when`(rateService.insertRate(anyOrNull())).thenReturn(Mono.just(created))

        testClient.post()
            .uri("/rate")
            .bodyValue(created)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(created.id.toString())
            .jsonPath("$.currency_from").isEqualTo(created.currency_from.toString())
            .jsonPath("$.currency_to").isEqualTo(created.currency_to.toString())
            .jsonPath("$.symbol").isEqualTo("GBPEUR")
            .jsonPath("$.rate").isEqualTo(1.13)

        verify(rateService, times(1)).insertRate(anyOrNull())
        verifyNoMoreInteractions(rateService)
    }

    @Test
    fun putRate() {
        `when`(rateService.updateRate(anyOrNull())).thenReturn(Mono.just(created))

        testClient.put()
            .uri("/rate")
            .bodyValue(created)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(created.id.toString())
            .jsonPath("$.currency_from").isEqualTo(created.currency_from.toString())
            .jsonPath("$.currency_to").isEqualTo(created.currency_to.toString())
            .jsonPath("$.symbol").isEqualTo("GBPEUR")
            .jsonPath("$.rate").isEqualTo(1.13)

        verify(rateService, times(1)).updateRate(anyOrNull())
        verifyNoMoreInteractions(rateService)
    }

    @Test
    fun getRate() {
        `when`(rateService.getRate(anyOrNull())).thenReturn(Mono.just(created))

        testClient.get()
            .uri("/rate/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(created.id.toString())
            .jsonPath("$.currency_from").isEqualTo(created.currency_from.toString())
            .jsonPath("$.currency_to").isEqualTo(created.currency_to.toString())
            .jsonPath("$.symbol").isEqualTo("GBPEUR")
            .jsonPath("$.rate").isEqualTo(1.13)

        verify(rateService, times(1)).getRate(anyOrNull())
        verifyNoMoreInteractions(rateService)
    }

    @Test
    fun getRates() {
        `when`(rateService.getRates()).thenReturn(Flux.just(created, ExchangeRate(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "GBPEUR", 1.13)))

        testClient.get()
            .uri("/rate/all")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].id").isEqualTo(created.id.toString())
            .jsonPath("$[0].currency_from").isEqualTo(created.currency_from.toString())
            .jsonPath("$[0].currency_to").isEqualTo(created.currency_to.toString())
            .jsonPath("$[0].symbol").isEqualTo("GBPEUR")
            .jsonPath("$[0].rate").isEqualTo(1.13)

        verify(rateService, times(1)).getRates()
        verifyNoMoreInteractions(rateService)
    }

    @Test
    fun deleteRate() {
        `when`(rateService.deleteRate(anyOrNull())).thenReturn(Mono.just(created))

        testClient.delete()
            .uri("/rate/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(created.id.toString())
            .jsonPath("$.currency_from").isEqualTo(created.currency_from.toString())
            .jsonPath("$.currency_to").isEqualTo(created.currency_to.toString())
            .jsonPath("$.symbol").isEqualTo("GBPEUR")
            .jsonPath("$.rate").isEqualTo(1.13)

        verify(rateService, times(1)).deleteRate(anyOrNull())
        verifyNoMoreInteractions(rateService)
    }
}