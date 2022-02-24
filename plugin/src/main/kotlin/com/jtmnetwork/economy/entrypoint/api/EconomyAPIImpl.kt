package com.jtmnetwork.economy.entrypoint.api

import com.google.inject.Inject
import com.jtm.framework.Framework
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.cache.ExchangeRateCache
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.TransactionService
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class EconomyAPIImpl @Inject constructor(private val framework: Framework, private val transactionService: TransactionService, private val walletCache: WalletCache, private val currencyCache: CurrencyCache, private val exchangeRateCache: ExchangeRateCache): EconomyAPI {

    private val walletService = walletCache.service

    override fun deposit(player: Player, currency: UUID, from: UUID?, amount: Double): Boolean {
        val wallet = walletCache.getById(player.uniqueId.toString()) ?: return false
        val transaction = walletCache.deposit(from, wallet, currency, amount) ?: return false
        framework.runTaskAsync { transactionService.insert(transaction) }
        return true
    }

    // Write documentation to have this method ran in a separate thread as will lock the main thread.
    override fun deposit(player: OfflinePlayer, currency: UUID, from: UUID?, amount: Double): Boolean {
        val wallet = walletService.get(player.uniqueId.toString()) ?: return false
        val transaction = walletCache.deposit(from, wallet, currency, amount) ?: return false
        transactionService.insert(transaction)
        return true
    }

    override fun withdraw(player: Player, currency: UUID, from: UUID?, amount: Double): Boolean {
        val wallet = walletCache.getById(player.uniqueId.toString()) ?: return false
        val transaction = walletCache.withdraw(from, wallet, currency, amount) ?: return false
        framework.runTaskAsync { transactionService.insert(transaction) }
        return true
    }

    override fun withdraw(player: OfflinePlayer, currency: UUID, from: UUID?, amount: Double): Boolean {
        val wallet = walletService.get(player.uniqueId.toString()) ?: return false
        val transaction = walletCache.withdraw(from, wallet, currency, amount) ?: return false
        transactionService.insert(transaction)
        return true
    }

    override fun balance(player: Player, currency: UUID): Double? {
        val wallet = walletCache.getById(player.uniqueId.toString()) ?: return null
        return wallet.getBalance(currency)
    }

    override fun balance(player: OfflinePlayer, currency: UUID): Double? {
        val wallet = walletService.get(player.uniqueId.toString()) ?: return null
        return wallet.getBalance(currency)
    }

    override fun getTransactions(player: Player, currency: UUID): List<Transaction> {
        return transactionService.getByReceiverAndCurrency(player.uniqueId, currency)
    }

    override fun getTransactions(player: OfflinePlayer, currency: UUID): List<Transaction> {
        return transactionService.getByReceiverAndCurrency(player.uniqueId, currency)
    }

    override fun getTransactions(player: Player): List<Transaction> {
        return transactionService.getByReceiver(player.uniqueId)
    }

    override fun getTransactions(player: OfflinePlayer): List<Transaction> {
        return transactionService.getByReceiver(player.uniqueId)
    }

    override fun exchangeAmount(player: Player, from: UUID, to: UUID, amount: Double): Boolean {
        val currencyFrom = currencyCache.getById(from) ?: return false
        val currencyTo = currencyCache.getById(to) ?: return false
        val rate = exchangeRateCache.getBySymbol("${currencyFrom.abbreviation}${currencyTo.abbreviation}") ?: return false
        val wallet = walletCache.getById(player.uniqueId.toString()) ?: return false

        val converted = amount * (rate.rate)
        val added = wallet.addBalance(to, converted) ?: return false
        val removed = added.removeBalance(from, amount) ?: return false

        val updated = walletCache.update(removed.id, removed) ?: return false
        framework.runTaskAsync { walletService.update(updated) }
        return true
    }

    override fun exchangeAmount(player: OfflinePlayer, from: UUID, to: UUID, amount: Double): Boolean {
        val currencyFrom = currencyCache.getById(from) ?: return false
        val currencyTo = currencyCache.getById(to) ?: return false
        val rate = exchangeRateCache.getBySymbol("${currencyFrom.abbreviation}${currencyTo.abbreviation}") ?: return false
        val wallet = walletService.get(player.uniqueId.toString()) ?: return false

        val converted = amount * (rate.rate)
        val added = wallet.addBalance(to, converted) ?: return false
        val removed = added.removeBalance(from, amount) ?: return false

        framework.runTaskAsync { walletService.update(removed) }
        return true
    }

    override fun getWallet(player: Player): Wallet? {
        return walletCache.getById(player.uniqueId.toString())
    }

    override fun getWallet(player: OfflinePlayer): Wallet? {
        return walletService.get(player.uniqueId.toString())
    }

    override fun getCurrency(id: UUID): Currency? {
        return currencyCache.getById(id)
    }

    override fun getCurrency(name: String): Currency? {
        return currencyCache.getByName(name)
    }

    override fun processRollback(player: Player) {
        TODO("Not yet implemented")
    }

    override fun processRollback(offlinePlayer: OfflinePlayer) {
        TODO("Not yet implemented")
    }
}