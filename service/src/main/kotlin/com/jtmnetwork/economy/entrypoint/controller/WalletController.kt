package com.jtmnetwork.economy.entrypoint.controller

import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/wallet")
class WalletController @Autowired constructor(private val walletService: WalletService) {

    @PostMapping
    fun postWallet(@RequestBody wallet: Wallet): Mono<Wallet> {
        return walletService.insertWallet(wallet)
    }

    @PutMapping
    fun putWallet(@RequestBody wallet: Wallet): Mono<Wallet> {
        return walletService.updateWallet(wallet)
    }

    @GetMapping("/{id}")
    fun getWallet(@PathVariable id: String): Mono<Wallet> {
        return walletService.getWallet(id)
    }

    @GetMapping("/name/{name}")
    fun getWalletByName(@PathVariable name: String): Mono<Wallet> {
        return walletService.getWalletByName(name)
    }

    @GetMapping("/all")
    fun getWallets(): Flux<Wallet> {
        return walletService.getWallets()
    }

    @GetMapping("/search/{prefix}")
    fun searchWallets(@PathVariable prefix: String): Flux<Wallet> {
        return walletService.searchWallets(prefix)
    }

    @DeleteMapping("/{id}")
    fun deleteWallet(@PathVariable id: String): Mono<Wallet> {
        return walletService.removeWallet(id)
    }
}