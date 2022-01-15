package com.jtmnetwork.economy.core.usecase.repository

import com.jtmnetwork.economy.core.domain.entity.Currency
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface CurrencyRepository: ReactiveMongoRepository<Currency, UUID> {

    fun findByName(name: String): Mono<Currency>
}