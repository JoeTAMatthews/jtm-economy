package com.jtmnetwork.economy.entrypoint.controller

import com.jtmnetwork.economy.core.domain.entity.ExchangeRate
import com.jtmnetwork.economy.data.service.RateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/rate")
class RateController @Autowired constructor(private val rateService: RateService) {

    @PostMapping
    fun postRate(@RequestBody rate: ExchangeRate): Mono<ExchangeRate> {
        return rateService.insertRate(rate)
    }

    @PutMapping
    fun putRate(@RequestBody rate: ExchangeRate): Mono<ExchangeRate> {
        return rateService.updateRate(rate)
    }

    @GetMapping("/{id}")
    fun getRate(@PathVariable id: UUID): Mono<ExchangeRate> {
        return rateService.getRate(id)
    }

    @GetMapping("/all")
    fun getRates(): Flux<ExchangeRate> {
        return rateService.getRates()
    }

    @DeleteMapping("/{id}")
    fun deleteRate(@PathVariable id: UUID): Mono<ExchangeRate> {
        return rateService.deleteRate(id)
    }
}