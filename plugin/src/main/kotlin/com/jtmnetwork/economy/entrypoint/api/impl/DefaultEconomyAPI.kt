package com.jtmnetwork.economy.entrypoint.api.impl

import com.google.inject.Inject
import com.jtm.framework.Framework
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.cache.ExchangeRateCache
import com.jtmnetwork.economy.data.service.TransactionService
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class DefaultEconomyAPI @Inject constructor(private val framework: Framework, private val transactionService: TransactionService, private val currencyCache: CurrencyCache, private val exchangeRateCache: ExchangeRateCache):
    EconomyAPI {

//    override fun getTransactions(player: Player, currency: UUID): List<Transaction> {
//        return transactionService.getByCurrency(player.uniqueId, currency)
//    }
//
//    override fun getTransactions(player: OfflinePlayer, currency: UUID): List<Transaction> {
//        return transactionService.getByCurrency(player.uniqueId, currency)
//    }
//
//    override fun getTransactions(player: Player): List<Transaction> {
//        return transactionService.getByReceiver(player.uniqueId)
//    }
//
//    override fun getTransactions(player: OfflinePlayer): List<Transaction> {
//        return transactionService.getByReceiver(player.uniqueId)
//    }

    override fun exchangeAmount(player: Player, from: UUID, to: UUID, amount: Double): Boolean {
        val optFrom = currencyCache.getById(from)
        if (optFrom.isEmpty) return false
        val currencyFrom = optFrom.get()

        val optTo = currencyCache.getById(to)
        if (optTo.isEmpty) return false
        val currencyTo = optTo.get()

        val symbol = "${currencyFrom.abbreviation}${currencyTo.abbreviation}"
        val optRate = exchangeRateCache.getBySymbol(symbol)
        if (optRate.isEmpty) return false
        val rate = optRate.get()

//        val optWallet = walletCache.getById(player.uniqueId.toString())
//        if (optWallet.isEmpty) return false
//        val wallet = optWallet.get()

//        if (!walletCache.hasBalance(player, from, amount)) return false

//        val converted = amount * (rate.rate)
//        val added = wallet.addBalance(to, converted) ?: return false
//        val removed = added.removeBalance(from, amount) ?: return false
//
//        val updated = walletCache.update(removed.id, removed)
//        updated.ifPresent { save -> framework.runTaskAsync { walletService.update(save) } }
        return true
    }

    override fun exchangeAmountOffline(player: OfflinePlayer, from: UUID, to: UUID, amount: Double): Boolean {
        val optFrom = currencyCache.getById(from)
        if (optFrom.isEmpty) return false
        val currencyFrom = optFrom.get()

        val optTo = currencyCache.getById(to)
        if (optTo.isEmpty) return false
        val currencyTo = optTo.get()

        val optRate = exchangeRateCache.getBySymbol("${currencyFrom.abbreviation}${currencyTo.abbreviation}")
        if (optRate.isEmpty) return false
        val rate = optRate.get()

//        val optWallet = walletService.get(player.uniqueId.toString())
//        if (optWallet.isEmpty) return false
//        val wallet = optWallet.get()

//        if (!walletCache.hasBalanceOffline(player, from, amount)) return false

//        val converted = amount * (rate.rate)
//        val added = wallet.addBalance(to, converted) ?: return false
//        val removed = added.removeBalance(from, amount) ?: return false
//
//        framework.runTaskAsync { walletService.update(removed) }
        return true
    }

    override fun getWallet(player: Player): Optional<Wallet> {
        return Optional.empty()
//        return walletCache.getById(player.uniqueId.toString())
    }

    override fun getWallet(player: OfflinePlayer): Optional<Wallet> {
        return Optional.empty()
//        return walletService.get(player.uniqueId.toString())
    }

    override fun getGlobalCurrency(): Optional<Currency> {
        return currencyCache.getGlobalCurrency()
    }

    override fun getCurrency(id: UUID): Optional<Currency> {
        return currencyCache.getById(id)
    }

    override fun getCurrency(name: String): Optional<Currency> {
        return currencyCache.getByName(name)
    }

    override fun getCurrencies(): List<Currency> {
        return currencyCache.getAll()
    }

    override fun processRollback(initiator: Player, target: Player, transactionId: Int): Boolean {
//        val optWallet = walletCache.getById(target.uniqueId.toString())
//        if (optWallet.isEmpty) return false
//        val wallet = optWallet.get()
//
//        val stack = transactionService.transactionsToStack(target.uniqueId)
//        if (stack.isEmpty()) {
//            initiator.sendMessage(UtilString.colour("&4Error: &cNo transactions to rollback."))
//            return false
//        }
//        var node = stack.pop()
//        while (transactionId != node.index || stack.isNotEmpty()) {
//            val transaction = node.transaction
//
//            val updated = wallet.setBalance(transaction.currency, transaction.previous_balance)
//            walletService.update(updated)
//
//            val deleted = transactionService.delete(transaction.id)
//            deleted.ifPresent { trans -> initiator.sendMessage(UtilString.colour("&cRolled back transaction: ${trans.id}")) }
//
//            if (stack.isNotEmpty()) node = stack.pop()
//        }
        return true
    }

    override fun processRollback(initiator: Player, target: OfflinePlayer, transactionId: Int): Boolean {
//        val optWallet = walletService.get(target.uniqueId.toString())
//        if (optWallet.isEmpty) return false
//        val wallet = optWallet.get()
//
//        val stack = transactionService.transactionsToStack(target.uniqueId)
//        if (stack.isEmpty()) {
//            initiator.sendMessage(UtilString.colour("&4Error: &cNo transactions to rollback."))
//            return false
//        }
//
//        var node = stack.pop()
//        while (transactionId != node.index || stack.isNotEmpty()) {
//            val transaction = node.transaction
//
//            val updated = wallet.setBalance(transaction.currency, transaction.previous_balance)
//            walletService.update(updated)
//
//            val deleted = transactionService.delete(transaction.id)
//            deleted.ifPresent { trans -> initiator.sendMessage(UtilString.colour("&cRolled back transaction: ${trans.id}")) }
//            if (stack.isNotEmpty()) node = stack.pop()
//        }
        return true
    }

    override fun pay(target: Player, sender: Player, currencyId: UUID, amount: Double): Boolean {
//        val optCurrency = currencyCache.getById(currencyId)
//        if (optCurrency.isEmpty) return false
//        val currency = optCurrency.get()
//
//        val optSenderWallet = walletCache.getById(sender.uniqueId.toString())
//        if (optSenderWallet.isEmpty) return false
//        val senderWallet = optSenderWallet.get()
//
//        val optReceiverWallet = walletCache.getById(target.uniqueId.toString())
//        if (optReceiverWallet.isEmpty) return false
//        val receiverWallet = optReceiverWallet.get()
//
//        if (!walletCache.hasBalance(sender, currencyId, amount)) {
//            sender.sendMessage(UtilString.colour("&4Error: &cInsufficient funds."))
//            return false
//        }
//
//        val removed = walletCache.withdraw(target.uniqueId, senderWallet, currency.id, amount) ?: return false
//        val added = walletCache.deposit(sender.uniqueId, receiverWallet, currency.id, amount) ?: return false
//        framework.runTaskAsync {
//            transactionService.insert(removed)
//            transactionService.insert(added)
//        }
        return true
    }

    override fun pay(target: OfflinePlayer, sender: Player, currencyId: UUID, amount: Double): Boolean {
//        val optCurrency = currencyCache.getById(currencyId)
//        if (optCurrency.isEmpty) return false
//        val currency = optCurrency.get()
//
//        val optSenderWallet = walletCache.getById(sender.uniqueId.toString())
//        if (optSenderWallet.isEmpty) return false
//        val senderWallet = optSenderWallet.get()
//
//        val optReceiverWallet = walletCache.getById(target.uniqueId.toString())
//        if (optReceiverWallet.isEmpty) return false
//        val receiverWallet = optReceiverWallet.get()
//
//        if (!walletCache.hasBalance(sender, currencyId, amount)) {
//            sender.sendMessage(UtilString.colour("&4Error: &cInsufficient funds."))
//            return false
//        }
//
//        framework.runTaskAsync {
//            val removed = walletCache.withdraw(target.uniqueId, senderWallet, currency.id, amount)
//            val added = walletCache.deposit(sender.uniqueId, receiverWallet, currency.id, amount)
//            transactionService.insert(removed)
//            transactionService.insert(added)
//        }
//
        return true
    }
}