package com.jtmnetwork.economy.entrypoint.controller

import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.data.service.CurrencyService
import org.junit.Test
import org.junit.runner.RunWith
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
@WebFluxTest(CurrencyController::class)
@AutoConfigureWebTestClient
class CurrencyControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var currencyService: CurrencyService

    private val created = Currency(UUID.randomUUID(), "British Pound", "GBP", "£")

    @Test
    fun postCurrency() {
        `when`(currencyService.insertCurrency(anyOrNull())).thenReturn(Mono.just(created))

        testClient.post()
            .uri("/currency")
            .bodyValue(created)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(created.id.toString())
            .jsonPath("$.name").isEqualTo(created.name)
            .jsonPath("$.abbreviation").isEqualTo(created.abbreviation)
            .jsonPath("$.symbol").isEqualTo(created.symbol)

        verify(currencyService, times(1)).insertCurrency(anyOrNull())
        verifyNoMoreInteractions(currencyService)
    }

    @Test
    fun putCurrency() {
        `when`(currencyService.updateCurrency(anyOrNull())).thenReturn(Mono.just(created))

        testClient.put()
            .uri("/currency")
            .bodyValue(created)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(created.id.toString())
            .jsonPath("$.name").isEqualTo(created.name)
            .jsonPath("$.abbreviation").isEqualTo(created.abbreviation)
            .jsonPath("$.symbol").isEqualTo(created.symbol)

        verify(currencyService, times(1)).updateCurrency(anyOrNull())
        verifyNoMoreInteractions(currencyService)
    }

    @Test
    fun getCurrency() {
        `when`(currencyService.getCurrency(anyOrNull())).thenReturn(Mono.just(created))

        testClient.get()
            .uri("/currency/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(created.id.toString())
            .jsonPath("$.name").isEqualTo(created.name)
            .jsonPath("$.abbreviation").isEqualTo(created.abbreviation)
            .jsonPath("$.symbol").isEqualTo(created.symbol)

        verify(currencyService, times(1)).getCurrency(anyOrNull())
        verifyNoMoreInteractions(currencyService)
    }

    @Test
    fun getCurrencies() {
        `when`(currencyService.getCurrencies()).thenReturn(Flux.just(created, Currency(UUID.randomUUID(), "Dollar", "USD", "$")))

        testClient.get()
            .uri("/currency/all")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].id").isEqualTo(created.id.toString())
            .jsonPath("$[0].name").isEqualTo(created.name)
            .jsonPath("$[0].abbreviation").isEqualTo(created.abbreviation)
            .jsonPath("$[0].symbol").isEqualTo(created.symbol)
            .jsonPath("$[1].name").isEqualTo("Dollar")
            .jsonPath("$[1].abbreviation").isEqualTo("USD")
            .jsonPath("$[1].symbol").isEqualTo("$")

        verify(currencyService, times(1)).getCurrencies()
        verifyNoMoreInteractions(currencyService)
    }

    @Test
    fun deleteCurrency() {
        `when`(currencyService.deleteCurrency(anyOrNull())).thenReturn(Mono.just(created))

        testClient.delete()
            .uri("/currency/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(created.id.toString())
            .jsonPath("$.name").isEqualTo(created.name)
            .jsonPath("$.abbreviation").isEqualTo(created.abbreviation)
            .jsonPath("$.symbol").isEqualTo(created.symbol)

        verify(currencyService, times(1)).deleteCurrency(anyOrNull())
        verifyNoMoreInteractions(currencyService)
    }
}