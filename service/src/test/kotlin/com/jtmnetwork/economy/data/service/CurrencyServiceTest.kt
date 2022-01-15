package com.jtmnetwork.economy.data.service

import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.exceptions.CurrencyFound
import com.jtmnetwork.economy.core.domain.exceptions.CurrencyNotFound
import com.jtmnetwork.economy.core.usecase.repository.CurrencyRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class CurrencyServiceTest {

    private val currencyRepository: CurrencyRepository = mock()
    private val currencyService = CurrencyService(currencyRepository)
    private val created = Currency(UUID.randomUUID(), "British Pound", "GBP", "£")

    @Test
    fun insertCurrency_thenFound() {
        `when`(currencyRepository.findByName(anyString())).thenReturn(Mono.just(created))

        val returned = currencyService.insertCurrency(created)

        verify(currencyRepository, times(1)).findByName(anyString())
        verifyNoMoreInteractions(currencyRepository)

        StepVerifier.create(returned)
            .expectError(CurrencyFound::class.java)
            .verify()
    }

    @Test
    fun insertCurrency() {
        `when`(currencyRepository.findByName(anyString())).thenReturn(Mono.empty())
        `when`(currencyRepository.save(anyOrNull())).thenReturn(Mono.just(created))

        val returned = currencyService.insertCurrency(created)

        verify(currencyRepository, times(1)).findByName(anyString())
        verifyNoMoreInteractions(currencyRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.name).isEqualTo("British Pound")
                assertThat(it.abbreviation).isEqualTo("GBP")
                assertThat(it.symbol).isEqualTo("£")
            }
            .verifyComplete()
    }

    @Test
    fun updateCurrency_thenNotFound() {
        `when`(currencyRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = currencyService.updateCurrency(created)

        verify(currencyRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(currencyRepository)

        StepVerifier.create(returned)
            .expectError(CurrencyNotFound::class.java)
            .verify()
    }

    @Test
    fun updateCurrency() {
        `when`(currencyRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(created))
        `when`(currencyRepository.save(anyOrNull())).thenReturn(Mono.just(created))

        val returned = currencyService.updateCurrency(created)

        verify(currencyRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(currencyRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.name).isEqualTo("British Pound")
                assertThat(it.abbreviation).isEqualTo("GBP")
                assertThat(it.symbol).isEqualTo("£")
            }
            .verifyComplete()
    }

    @Test
    fun getCurrency_thenNotFound() {
        `when`(currencyRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = currencyService.getCurrency(UUID.randomUUID())

        verify(currencyRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(currencyRepository)

        StepVerifier.create(returned)
            .expectError(CurrencyNotFound::class.java)
            .verify()
    }

    @Test
    fun getCurrency() {
        `when`(currencyRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(created))

        val returned = currencyService.getCurrency(UUID.randomUUID())

        verify(currencyRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(currencyRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.name).isEqualTo("British Pound")
                assertThat(it.abbreviation).isEqualTo("GBP")
                assertThat(it.symbol).isEqualTo("£")
            }
            .verifyComplete()
    }

    @Test
    fun getCurrencies() {
        `when`(currencyRepository.findAll()).thenReturn(Flux.just(created, Currency(UUID.randomUUID(), "Dollar", "USD", "$")))

        val returned = currencyService.getCurrencies()

        verify(currencyRepository, times(1)).findAll()
        verifyNoMoreInteractions(currencyRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.name).isEqualTo("British Pound")
                assertThat(it.abbreviation).isEqualTo("GBP")
                assertThat(it.symbol).isEqualTo("£")
            }
            .assertNext {
                assertThat(it.name).isEqualTo("Dollar")
                assertThat(it.abbreviation).isEqualTo("USD")
                assertThat(it.symbol).isEqualTo("$")
            }
            .verifyComplete()
    }

    @Test
    fun deleteCurrency_thenNotFound() {
        `when`(currencyRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = currencyService.deleteCurrency(UUID.randomUUID())

        verify(currencyRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(currencyRepository)

        StepVerifier.create(returned)
            .expectError(CurrencyNotFound::class.java)
            .verify()
    }

    @Test
    fun deleteCurrency() {
        `when`(currencyRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(created))
        `when`(currencyRepository.delete(anyOrNull())).thenReturn(Mono.empty())

        val returned = currencyService.deleteCurrency(UUID.randomUUID())

        verify(currencyRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(currencyRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.name).isEqualTo("British Pound")
                assertThat(it.abbreviation).isEqualTo("GBP")
                assertThat(it.symbol).isEqualTo("£")
            }
            .verifyComplete()
    }
}