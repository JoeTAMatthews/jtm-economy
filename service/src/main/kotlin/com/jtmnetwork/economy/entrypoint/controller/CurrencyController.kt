package com.jtmnetwork.economy.entrypoint.controller

import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.data.service.CurrencyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/currency")
class CurrencyController @Autowired constructor(private val currencyService: CurrencyService) {

    @PostMapping
    fun postCurrency(@RequestBody currency: Currency): Mono<Currency> {
        return currencyService.insertCurrency(currency)
    }

    @PutMapping
    fun putCurrency(@RequestBody currency: Currency): Mono<Currency> {
        return currencyService.updateCurrency(currency)
    }

    @GetMapping("/{id}")
    fun getCurrency(@PathVariable id: UUID): Mono<Currency> {
        return currencyService.getCurrency(id)
    }

    @GetMapping("/all")
    fun getCurrencies(): Flux<Currency> {
        return currencyService.getCurrencies()
    }

    @DeleteMapping("/{id}")
    fun deleteCurrency(@PathVariable id: UUID): Mono<Currency> {
        return currencyService.deleteCurrency(id)
    }
}