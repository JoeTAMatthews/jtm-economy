package com.jtmnetwork.economy.entrypoint.api

import com.google.inject.Inject
import com.jtm.framework.Framework
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.TransactionService
import com.jtmnetwork.economy.data.service.WalletService
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class EconomyAPIImpl @Inject constructor(private val framework: Framework, private val transactionService: TransactionService, private val walletCache: WalletCache, private val currencyCache: CurrencyCache): EconomyAPI {

    private val service = walletCache.service

    override fun deposit(player: Player, currency: UUID, from: UUID?, amount: Double): Boolean {
        val wallet = walletCache.getById(player.uniqueId) ?: return false
        val transaction = walletCache.deposit(wallet, currency, amount) ?: return false
        framework.runTaskAsync { transactionService.insert(transaction) }
        return true
    }

    // Write documentation to have this method ran in a separate thread as will lock the main thread.
    override fun deposit(player: OfflinePlayer, currency: UUID, from: UUID?, amount: Double): Boolean {
        val wallet = service.get(player.uniqueId) ?: return false
        val transaction = walletCache.deposit(wallet, currency, amount) ?: return false
        transactionService.insert(transaction)
        return true
    }

    override fun withdraw(player: Player, currency: UUID, from: UUID?, amount: Double): Boolean {
        val wallet = walletCache.getById(player.uniqueId) ?: return false
        val transaction = walletCache.withdraw(wallet, currency, amount) ?: return false
        framework.runTaskAsync { transactionService.insert(transaction) }
        return true
    }

    override fun withdraw(player: OfflinePlayer, currency: UUID, from: UUID?, amount: Double): Boolean {
        val wallet = service.get(player.uniqueId) ?: return false
        val transaction = walletCache.withdraw(wallet, currency, amount) ?: return false
        transactionService.insert(transaction)
        return true
    }

    override fun balance(player: Player, currency: UUID): Double? {
        val wallet = walletCache.getById(player.uniqueId) ?: return null
        return wallet.getBalance(currency)
    }

    override fun balance(player: OfflinePlayer, currency: UUID): Double? {
        val wallet = service.get(player.uniqueId) ?: return null
        return wallet.getBalance(currency)
    }

    override fun getTransactions(player: Player, currency: UUID): List<Transaction> {
        return transactionService.getByPlayerIdAndCurrency(player.uniqueId, currency)
    }

    override fun getTransactions(player: OfflinePlayer, currency: UUID): List<Transaction> {
        return transactionService.getByPlayerIdAndCurrency(player.uniqueId, currency)
    }

    override fun getTransactions(player: Player): List<Transaction> {
        return transactionService.getByPlayerId(player.uniqueId)
    }

    override fun getTransactions(player: OfflinePlayer): List<Transaction> {
        return transactionService.getByPlayerId(player.uniqueId)
    }
}