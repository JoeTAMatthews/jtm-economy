package com.jtmnetwork.economy.core.usecase.repository

import com.jtmnetwork.economy.core.domain.entity.Wallet
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface WalletRepository: ReactiveMongoRepository<Wallet, String> {

    fun findByName(name: String): Mono<Wallet>
}