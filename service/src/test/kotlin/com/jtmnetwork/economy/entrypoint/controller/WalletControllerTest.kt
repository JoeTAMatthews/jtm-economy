package com.jtmnetwork.economy.entrypoint.controller

import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.service.WalletService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(WalletController::class, excludeAutoConfiguration = [ReactiveSecurityAutoConfiguration::class])
@AutoConfigureWebTestClient
class WalletControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var walletService: WalletService

    private val wallet = Wallet(UUID.randomUUID().toString(), "JTY", created = System.currentTimeMillis())

    @Test
    fun postWallet() {
        `when`(walletService.insertWallet(anyOrNull())).thenReturn(Mono.just(wallet))

        testClient.post()
            .uri("/wallet")
            .bodyValue(wallet)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(wallet.id)
            .jsonPath("$.name").isEqualTo(wallet.name)
            .jsonPath("$.created").isEqualTo(wallet.created)

        verify(walletService, times(1)).insertWallet(anyOrNull())
        verifyNoMoreInteractions(walletService)
    }

    @Test
    fun putWallet() {
        `when`(walletService.updateWallet(anyOrNull())).thenReturn(Mono.just(wallet))

        testClient.put()
            .uri("/wallet")
            .bodyValue(wallet)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(wallet.id)
            .jsonPath("$.name").isEqualTo(wallet.name)
            .jsonPath("$.created").isEqualTo(wallet.created)

        verify(walletService, times(1)).updateWallet(anyOrNull())
        verifyNoMoreInteractions(walletService)
    }

    @Test
    fun getWallet() {
        `when`(walletService.getWallet(anyString())).thenReturn(Mono.just(wallet))

        testClient.get()
            .uri("/wallet/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(wallet.id)
            .jsonPath("$.name").isEqualTo(wallet.name)
            .jsonPath("$.created").isEqualTo(wallet.created)

        verify(walletService, times(1)).getWallet(anyString())
        verifyNoMoreInteractions(walletService)
    }

    @Test
    fun getWallets() {
        `when`(walletService.getWallets()).thenReturn(Flux.just(wallet))

        testClient.get()
            .uri("/wallet/all")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].id").isEqualTo(wallet.id)
            .jsonPath("$[0].name").isEqualTo(wallet.name)
            .jsonPath("$[0].created").isEqualTo(wallet.created)

        verify(walletService, times(1)).getWallets()
        verifyNoMoreInteractions(walletService)
    }

    @Test
    fun deleteWallet() {
        `when`(walletService.removeWallet(anyString())).thenReturn(Mono.just(wallet))

        testClient.delete()
            .uri("/wallet/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(wallet.id)
            .jsonPath("$.name").isEqualTo(wallet.name)
            .jsonPath("$.created").isEqualTo(wallet.created)

        verify(walletService, times(1)).removeWallet(anyString())
        verifyNoMoreInteractions(walletService)
    }
}