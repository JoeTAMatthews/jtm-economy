package com.jtmnetwork.economy.data.service

import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.exceptions.CurrencyFound
import com.jtmnetwork.economy.core.domain.exceptions.CurrencyNotFound
import com.jtmnetwork.economy.core.usecase.repository.CurrencyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class CurrencyService @Autowired constructor(private val currencyRepository: CurrencyRepository) {

    fun insertCurrency(currency: Currency): Mono<Currency> {
        return currencyRepository.findByName(currency.name)
            .flatMap<Currency> { Mono.error(CurrencyFound()) }
            .switchIfEmpty(Mono.defer { currencyRepository.save(currency) })
    }

    fun updateCurrency(currency: Currency): Mono<Currency> {
        return currencyRepository.findById(currency.id)
            .switchIfEmpty(Mono.defer { Mono.error(CurrencyNotFound()) })
            .flatMap { currencyRepository.save(it.update(currency)) }
    }

    fun getCurrency(id: UUID): Mono<Currency> {
        return currencyRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(CurrencyNotFound()) })
    }

    fun getCurrencies(): Flux<Currency> {
        return currencyRepository.findAll()
    }

    fun deleteCurrency(id: UUID): Mono<Currency> {
        return currencyRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(CurrencyNotFound()) })
            .flatMap { currencyRepository.delete(it).thenReturn(it) }
    }
}