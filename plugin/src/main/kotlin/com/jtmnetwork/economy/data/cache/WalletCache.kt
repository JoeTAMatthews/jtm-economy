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
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

@Singleton
class WalletCache @Inject constructor(private val framework: Framework, val service: WalletService, connector: RedisConnector): CacheService<Wallet, String>(connector,"wallet", Wallet::class.java) {

    private val logging = framework.getLogging()

    fun enable() {
        Bukkit.getServer().onlinePlayers.forEach {
            val wallet = service.get(it.uniqueId.toString()) ?: return
            insert(it.uniqueId.toString(), wallet)
        }
    }

    fun disable() {
        logging.info("Saving wallets:")
        getAll()?.forEach {
            val wallet = service.update(it) ?: return
            remove(wallet.id)
            logging.info("- ${wallet.id}")
        }
    }

    fun hasBalance(player: Player, currency: UUID, amount: Double): Boolean {
        val wallet = getById(player.uniqueId.toString()) ?: return false
        val balance = wallet.balances[currency] ?: return false
        return balance >= amount
    }

    fun hasBalance(player: OfflinePlayer, currency: UUID, amount: Double): Boolean {
        val wallet = service.get(player.uniqueId.toString()) ?: return false
        val balance = wallet.balances[currency] ?: return false
        return balance >= amount
    }

    fun deposit(sender: UUID?, wallet: Wallet, currency: UUID, amount: Double): Transaction? {
        val current = wallet.getBalance(currency)
        val deposited = wallet.addBalance(currency, amount) ?: return null
        if (exists(wallet.id)) update(wallet.id, wallet)
        framework.runTaskAsync { service.update(deposited) }
        return Transaction(type = TransactionType.IN, sender = sender, receiver = UUID.fromString(wallet.id), currency = currency, amount = amount, previous_balance = current, new_balance = deposited.getBalance(currency))
    }

    fun withdraw(sender: UUID?, wallet: Wallet, currency: UUID, amount: Double): Transaction? {
        val current = wallet.getBalance(currency)
        val withdrew = wallet.removeBalance(currency, amount) ?: return null
        if (exists(wallet.id)) update(wallet.id, wallet)
        framework.runTaskAsync { service.update(withdrew) }
        return Transaction(type = TransactionType.OUT, sender = UUID.fromString(wallet.id), receiver = sender, currency = currency, amount = amount, previous_balance = current, new_balance = withdrew.getBalance(currency))
    }
}