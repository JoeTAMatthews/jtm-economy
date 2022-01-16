package com.jtmnetwork.economy.data.service

import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.core.domain.exceptions.WalletFound
import com.jtmnetwork.economy.core.domain.exceptions.WalletNotFound
import com.jtmnetwork.economy.core.usecase.repository.WalletRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class WalletService @Autowired constructor(private val walletRepository: WalletRepository) {

    fun insertWallet(wallet: Wallet): Mono<Wallet> {
        return walletRepository.findById(wallet.id)
            .flatMap<Wallet?> { Mono.defer { Mono.error(WalletFound()) } }
            .switchIfEmpty(Mono.defer { walletRepository.save(wallet) })
    }

    fun updateWallet(wallet: Wallet): Mono<Wallet> {
        return walletRepository.findById(wallet.id)
            .switchIfEmpty(Mono.defer { Mono.error(WalletNotFound()) })
            .flatMap { walletRepository.save(it.update(wallet)) }
    }

    fun getWallet(id: String): Mono<Wallet> {
        return walletRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(WalletNotFound()) })
    }

    fun getWallets(): Flux<Wallet> {
        return walletRepository.findAll()
    }

    fun removeWallet(id: String): Mono<Wallet> {
        return walletRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(WalletNotFound()) })
            .flatMap { walletRepository.delete(it).thenReturn(it) }
    }
}