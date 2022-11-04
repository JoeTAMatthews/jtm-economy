package com.jtmnetwork.economy.data.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.database.DatabaseConnector
import com.jtm.framework.data.service.Service
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.entity.Wallet
import okhttp3.internal.format
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import java.util.*

@Singleton
class WalletService @Inject constructor(framework: Framework, connector: DatabaseConnector, private val transactionService: TransactionService): Service<Wallet, String>(framework, connector, Wallet::class.java) {

    private val logging = framework.getLogging()
    private val messenger = framework.getLocaleMessenger()

    /**
     * Deposit an amount of currency to the target player's wallet, send error
     * messages to the command sender. If the {@param from} is null will be considered
     * a server sent deposit.
     *
     * @param sender    the command sender.
     * @param player    the target offline player.
     * @param from      the nullable sender of the currency amount
     * @param currency  the selected currency.
     * @param amount    the amount of currency.
     *
     * @return          the transaction of the deposit.
     */
    fun deposit(sender: CommandSender?, player: OfflinePlayer, from: UUID?, currency: Currency, amount: Double): Optional<Transaction> {
        val opt = get(player.uniqueId.toString())
        if (opt.isEmpty) {
            if (sender != null) messenger.sendMessage(sender, "economy.error.failed_finding_wallet")
            logging.debug(format("%s(%s) wallet was not found.", player.uniqueId.toString(), player.name ?: "no-name"))
            return Optional.empty()
        }
        val wallet = opt.get()
        val transaction = wallet.deposit(from, currency.id, amount)
        if (transaction == null) {
            if (sender != null) messenger.sendMessage(sender, "economy.deposit.sender_failed")
            logging.debug(format("Failed to deposit in wallet %s(%s)", player.uniqueId.toString(), player.name ?: "no-name"))
            return Optional.empty()
        }

        if (sender != null) messenger.sendMessage(sender, "economy.deposit.sender_success", currency.getSymbolAmount(amount), player.name)
        framework.runTaskAsync { transactionService.insert(transaction) }
        return Optional.of(transaction)
    }

    /**
     * Withdraw an amount of currency from the target player's wallet, send error
     * messages to the command sender. If the {@param to} is null will be considered
     * a server sent withdrawal.
     *
     * @param sender    the command sender.
     * @param player    the target player.
     * @param to        the nullable receiver of the currency amount.
     * @param currency  the selected currency.
     * @param amount    the amount to withdraw.
     *
     * @return          the transaction of the withdrawal.
     */
    fun withdraw(sender: CommandSender?, player: OfflinePlayer, to: UUID?, currency: Currency, amount: Double): Optional<Transaction> {
        val opt = get(player.uniqueId.toString())
        if (opt.isEmpty) {
            if (sender != null) messenger.sendMessage(sender, "economy.error.failed_finding_wallet")
            logging.debug(format("%s(%s) wallet was not found.", player.uniqueId.toString(), player.name ?: "no-name"))
            return Optional.empty()
        }

        val wallet = opt.get()
        val transaction = wallet.withdraw(to, currency.id, amount)
        if (transaction == null) {
            if (sender != null) messenger.sendMessage(sender, "economy.withdraw.sender_failed")
            logging.debug(format("Failed to withdraw from wallet %s(%s)", player.uniqueId.toString(), player.name ?: "no-name"))
            return Optional.empty()
        }

        if (sender != null) messenger.sendMessage(sender, "economy.withdraw.sender_success", currency.getSymbolAmount(amount), player.name)
        framework.runTaskAsync { transactionService.insert(transaction) }
        return Optional.of(transaction)
    }

    /**
     * Return the balance of the currency selected under the target players wallet.
     * Sending error messages to the command sender.
     *
     * @param sender    the command sender.
     * @param player    the target player.
     * @param currency  the currency selected.
     *
     * @return          the balance of the currency selected under the target wallet.
     */
    fun balance(sender: CommandSender?, player: OfflinePlayer, currency: Currency): Optional<Double> {
        val opt = get(player.uniqueId.toString())
        if (opt.isEmpty) {
            if (sender != null) messenger.sendMessage(sender, "economy.error.failed_finding_wallet")
            logging.debug(format("%s(%s) wallet was not found.", player.uniqueId.toString(), player.name ?: "no-name"))
            return Optional.empty()
        }

        val wallet = opt.get()
        val balance = wallet.getBalance(currency.id)
        if (sender != null) messenger.sendMessage(sender, "economy.balance", player.name, balance)
        return Optional.of(balance)
    }

    /**
     * Check the balance of an offline player against the currency & amount provided, sending error
     * messages to the command sender.
     *
     * @param sender    the command sender.
     * @param player    the target player.
     * @param currency  the currency selected.
     * @param amount    the amount to be checked.
     *
     * @return          if the player has sufficient funds return true, if they don't return false.
     */
    fun hasBalance(sender: CommandSender?, player: OfflinePlayer, currency: Currency, amount: Double): Boolean {
        val opt = get(player.uniqueId.toString())
        if (opt.isEmpty) {
            if (sender != null) messenger.sendMessage(sender, "economy.error.failed_finding_wallet")
            logging.debug(format("%s(%s) wallet was not found.", player.uniqueId.toString(), player.name ?: "no-name"))
            return false
        }

        val wallet = opt.get()
        return wallet.checkBalance(currency.id, amount)
    }

    fun getWallet(sender: CommandSender?, player: OfflinePlayer): Optional<Wallet> {
        val opt = get(player.uniqueId.toString())
        if (opt.isEmpty) {
            if (sender != null) messenger.sendMessage(sender, "economy.error.failed_finding_wallet")
            logging.debug(format("%s(%s) wallet was not found.", player.uniqueId.toString(), player.name ?: "no-name"))
            return Optional.empty()
        }

        return opt
    }
}