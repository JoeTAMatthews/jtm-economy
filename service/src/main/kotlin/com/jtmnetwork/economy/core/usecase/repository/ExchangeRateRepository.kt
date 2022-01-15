package com.jtmnetwork.economy.core.usecase.repository

import com.jtmnetwork.economy.core.domain.entity.ExchangeRate
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface ExchangeRateRepository: ReactiveMongoRepository<ExchangeRate, UUID> {

    fun findBySymbol(symbol: String): Mono<ExchangeRate>
}