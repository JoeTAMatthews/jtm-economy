package com.jtmnetwork.economy.data.service

import com.jtmnetwork.economy.core.domain.entity.ExchangeRate
import com.jtmnetwork.economy.core.domain.exceptions.RateFound
import com.jtmnetwork.economy.core.domain.exceptions.RateNotFound
import com.jtmnetwork.economy.core.usecase.repository.ExchangeRateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class RateService @Autowired constructor(private val rateRepository: ExchangeRateRepository) {

    fun insertRate(rate: ExchangeRate): Mono<ExchangeRate> {
        return rateRepository.findBySymbol(rate.symbol)
            .flatMap<ExchangeRate?> { Mono.defer { Mono.error(RateFound()) } }
            .switchIfEmpty(Mono.defer { rateRepository.save(rate) })
    }

    fun updateRate(rate: ExchangeRate): Mono<ExchangeRate> {
        return rateRepository.findById(rate.id)
            .switchIfEmpty(Mono.defer { Mono.error(RateNotFound()) })
            .flatMap { rateRepository.save(it.update(rate)) }
    }

    fun getRate(id: UUID): Mono<ExchangeRate> {
        return rateRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(RateNotFound()) })
    }

    fun getRates(): Flux<ExchangeRate> {
        return rateRepository.findAll()
    }

    fun deleteRate(id: UUID): Mono<ExchangeRate> {
        return rateRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(RateNotFound()) })
            .flatMap { rateRepository.delete(it).thenReturn(it) }
    }
}