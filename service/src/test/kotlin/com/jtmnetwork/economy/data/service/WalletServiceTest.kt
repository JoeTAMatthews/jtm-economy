package com.jtmnetwork.economy.data.service

import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.core.domain.exceptions.WalletFound
import com.jtmnetwork.economy.core.domain.exceptions.WalletNotFound
import com.jtmnetwork.economy.core.usecase.repository.WalletRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class WalletServiceTest {

    private val walletRepository: WalletRepository = mock()
    private val walletService = WalletService(walletRepository)
    private val wallet = Wallet(UUID.randomUUID().toString(), "JTY", created = System.currentTimeMillis())

    @Test
    fun insertWallet_thenFound() {
        `when`(walletRepository.findById(anyString())).thenReturn(Mono.just(wallet))

        val returned = walletService.insertWallet(wallet)

        verify(walletRepository, times(1)).findById(anyString())
        verifyNoMoreInteractions(walletRepository)

        StepVerifier.create(returned)
            .expectError(WalletFound::class.java)
            .verify()
    }

    @Test
    fun insertWallet() {
        `when`(walletRepository.findById(anyString())).thenReturn(Mono.empty())
        `when`(walletRepository.save(anyOrNull())).thenReturn(Mono.just(wallet))

        val returned = walletService.insertWallet(wallet)

        verify(walletRepository, times(1)).findById(anyString())
        verifyNoMoreInteractions(walletRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(wallet.id)
                assertThat(it.name).isEqualTo(wallet.name)
                assertThat(it.created).isEqualTo(wallet.created)
            }
            .verifyComplete()
    }

    @Test
    fun updateWallet_thenNotFound() {
        `when`(walletRepository.findById(anyString())).thenReturn(Mono.empty())

        val returned = walletService.updateWallet(wallet)

        verify(walletRepository, times(1)).findById(anyString())
        verifyNoMoreInteractions(walletRepository)

        StepVerifier.create(returned)
            .expectError(WalletNotFound::class.java)
            .verify()
    }

    @Test
    fun updateWallet() {
        `when`(walletRepository.findById(anyString())).thenReturn(Mono.just(wallet))
        `when`(walletRepository.save(anyOrNull())).thenReturn(Mono.just(wallet))

        val returned = walletService.updateWallet(wallet)

        verify(walletRepository, times(1)).findById(anyString())
        verifyNoMoreInteractions(walletRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(wallet.id)
                assertThat(it.name).isEqualTo(wallet.name)
                assertThat(it.created).isEqualTo(wallet.created)
            }
            .verifyComplete()
    }

    @Test
    fun getWallet_thenNotFound() {
        `when`(walletRepository.findById(anyString())).thenReturn(Mono.empty())

        val returned = walletService.getWallet(UUID.randomUUID().toString())

        verify(walletRepository, times(1)).findById(anyString())
        verifyNoMoreInteractions(walletRepository)

        StepVerifier.create(returned)
            .expectError(WalletNotFound::class.java)
            .verify()
    }

    @Test
    fun getWallet() {
        `when`(walletRepository.findById(anyString())).thenReturn(Mono.just(wallet))

        val returned = walletService.getWallet(UUID.randomUUID().toString())

        verify(walletRepository, times(1)).findById(anyString())
        verifyNoMoreInteractions(walletRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(wallet.id)
                assertThat(it.name).isEqualTo(wallet.name)
                assertThat(it.created).isEqualTo(wallet.created)
            }
            .verifyComplete()
    }

    @Test
    fun getWallets() {
        `when`(walletRepository.findAll()).thenReturn(Flux.just(wallet))

        val returned = walletService.getWallets()

        verify(walletRepository, times(1)).findAll()
        verifyNoMoreInteractions(walletRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(wallet.id)
                assertThat(it.name).isEqualTo(wallet.name)
                assertThat(it.created).isEqualTo(wallet.created)
            }
            .verifyComplete()
    }

    @Test
    fun removeWallet_thenNotFound() {
        `when`(walletRepository.findById(anyString())).thenReturn(Mono.empty())

        val returned = walletService.removeWallet(UUID.randomUUID().toString())

        verify(walletRepository, times(1)).findById(anyString())
        verifyNoMoreInteractions(walletRepository)

        StepVerifier.create(returned)
            .expectError(WalletNotFound::class.java)
            .verify()
    }

    @Test
    fun removeWallet() {
        `when`(walletRepository.findById(anyString())).thenReturn(Mono.just(wallet))
        `when`(walletRepository.delete(anyOrNull())).thenReturn(Mono.empty())

        val returned = walletService.removeWallet(UUID.randomUUID().toString())

        verify(walletRepository, times(1)).findById(anyString())
        verifyNoMoreInteractions(walletRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(wallet.id)
                assertThat(it.name).isEqualTo(wallet.name)
                assertThat(it.created).isEqualTo(wallet.created)
            }
            .verifyComplete()
    }
}