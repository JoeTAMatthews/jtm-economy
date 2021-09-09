package com.jtmnetwork.economy.data.cache

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.redis.RedisConnector
import com.jtm.framework.data.service.CacheService
import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.service.WalletService
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

@Singleton
class WalletCache @Inject constructor(private val framework: Framework, val service: WalletService, connector: RedisConnector): CacheService<Wallet, UUID>(connector,"wallet", Wallet::class.java) {

    fun enable() {
        Bukkit.getServer().onlinePlayers.forEach {
            val wallet = service.get(it.uniqueId) ?: return
            insert(it.uniqueId, wallet)
        }
    }

    fun disable() {
        getAll()?.forEach {
            service.update(it)
            remove(it.id)
        }
    }

    fun hasBalance(player: Player, currency: UUID, amount: Double): Boolean {
        val wallet = getById(player.uniqueId) ?: return false
        val balance = wallet.balances[currency] ?: return false
        return amount >= balance
    }

    fun deposit(wallet: Wallet, currency: UUID, amount: Double): Transaction? {
        val deposited = wallet.addBalance(currency, amount) ?: return null
        framework.runTaskAsync { service.update(deposited) }
        return Transaction(type = TransactionType.IN, playerId = wallet.id, currency = currency, amount = amount, balance = deposited.getBalance(currency))
    }

    fun withdraw(wallet: Wallet, currency: UUID, amount: Double): Transaction? {
        val withdrew = wallet.removeBalance(currency, amount) ?: return null
        framework.runTaskAsync { service.update(withdrew) }
        return Transaction(type = TransactionType.OUT, playerId = wallet.id, currency = currency, amount = amount, balance = withdrew.getBalance(currency))
    }
}