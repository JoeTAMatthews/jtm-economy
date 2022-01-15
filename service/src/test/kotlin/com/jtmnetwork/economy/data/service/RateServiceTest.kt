package com.jtmnetwork.economy.data.service

import com.jtmnetwork.economy.core.domain.entity.ExchangeRate
import com.jtmnetwork.economy.core.domain.exceptions.RateFound
import com.jtmnetwork.economy.core.domain.exceptions.RateNotFound
import com.jtmnetwork.economy.core.usecase.repository.ExchangeRateRepository
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
class RateServiceTest {

    private val rateRepository: ExchangeRateRepository = mock()
    private val rateService = RateService(rateRepository)
    private val created = ExchangeRate(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "GBPEUR", 1.14)

    @Test
    fun insertRate_thenFound() {
        `when`(rateRepository.findBySymbol(anyString())).thenReturn(Mono.just(created))

        val returned = rateService.insertRate(created)

        verify(rateRepository, times(1)).findBySymbol(anyString())
        verifyNoMoreInteractions(rateRepository)

        StepVerifier.create(returned)
            .expectError(RateFound::class.java)
            .verify()
    }

    @Test
    fun insertRate() {
        `when`(rateRepository.findBySymbol(anyString())).thenReturn(Mono.empty())
        `when`(rateRepository.save(anyOrNull())).thenReturn(Mono.just(created))

        val returned = rateService.insertRate(created)

        verify(rateRepository, times(1)).findBySymbol(anyString())
        verifyNoMoreInteractions(rateRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.currency_from).isEqualTo(created.currency_from)
                assertThat(it.currency_to).isEqualTo(created.currency_to)
                assertThat(it.symbol).isEqualTo(created.symbol)
                assertThat(it.rate).isEqualTo(created.rate)
            }
            .verifyComplete()
    }

    @Test
    fun updateRate_thenNotFound() {
        `when`(rateRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = rateService.updateRate(created)

        verify(rateRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(rateRepository)

        StepVerifier.create(returned)
            .expectError(RateNotFound::class.java)
            .verify()
    }

    @Test
    fun updateRate() {
        `when`(rateRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(created))

        val returned = rateService.updateRate(created)

        verify(rateRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(rateRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.currency_from).isEqualTo(created.currency_from)
                assertThat(it.currency_to).isEqualTo(created.currency_to)
                assertThat(it.symbol).isEqualTo(created.symbol)
                assertThat(it.rate).isEqualTo(created.rate)
            }
            .verifyComplete()
    }

    @Test
    fun getRate_thenNotFound() {
        `when`(rateRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = rateService.getRate(UUID.randomUUID())

        verify(rateRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(rateRepository)

        StepVerifier.create(returned)
            .expectError(RateNotFound::class.java)
            .verify()
    }

    @Test
    fun getRate() {
        `when`(rateRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(created))

        val returned = rateService.getRate(UUID.randomUUID())

        verify(rateRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(rateRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.currency_from).isEqualTo(created.currency_from)
                assertThat(it.currency_to).isEqualTo(created.currency_to)
                assertThat(it.symbol).isEqualTo(created.symbol)
                assertThat(it.rate).isEqualTo(created.rate)
            }
            .verifyComplete()
    }

    @Test
    fun getRates() {
        `when`(rateRepository.findAll()).thenReturn(Flux.just(created, ExchangeRate(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "EURGBP", 0.85)))

        val returned = rateService.getRate(UUID.randomUUID())

        verify(rateRepository, times(1)).findAll()
        verifyNoMoreInteractions(rateRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.currency_from).isEqualTo(created.currency_from)
                assertThat(it.currency_to).isEqualTo(created.currency_to)
                assertThat(it.symbol).isEqualTo(created.symbol)
                assertThat(it.rate).isEqualTo(created.rate)
            }
            .assertNext {
                assertThat(it.symbol).isEqualTo("EURGBP")
                assertThat(it.rate).isEqualTo(0.85)
            }
            .verifyComplete()

    }

    @Test
    fun deleteRate_thenNotFound() {
        `when`(rateRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = rateService.deleteRate(UUID.randomUUID())

        verify(rateRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(rateRepository)

        StepVerifier.create(returned)
            .expectError(RateNotFound::class.java)
            .verify()
    }

    @Test
    fun deleteRate() {
        `when`(rateRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(created))
        `when`(rateRepository.delete(anyOrNull())).thenReturn(Mono.empty())

        val returned = rateService.deleteRate(UUID.randomUUID())

        verify(rateRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(rateRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.currency_from).isEqualTo(created.currency_from)
                assertThat(it.currency_to).isEqualTo(created.currency_to)
                assertThat(it.symbol).isEqualTo(created.symbol)
                assertThat(it.rate).isEqualTo(created.rate)
            }
            .verifyComplete()
    }
}