package com.jtmnetwork.economy.data.cache

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.redis.RedisConnector
import com.jtm.framework.data.service.CacheService
import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.service.TransactionService
import com.jtmnetwork.economy.data.service.WalletService
import okhttp3.internal.format
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@Singleton
class WalletCache @Inject constructor(private val framework: Framework, val service: WalletService, val transactionService: TransactionService, connector: RedisConnector): CacheService<Wallet, String>(connector,"wallet", Wallet::class.java) {

    private val logging = framework.getLogging()
    private val messenger = framework.getLocaleMessenger()

    fun enable() {
        Bukkit.getServer().onlinePlayers.forEach {
            val opt = service.get(it.uniqueId.toString())
            opt.ifPresent { wallet -> insert(it.uniqueId.toString(), wallet) }
        }
    }

    fun disable() {
        logging.info("Saving wallets:")
        getAll().forEach {
            val opt = service.update(it)
            opt.ifPresent { wallet ->
                remove(wallet.id)
                logging.info("- ${wallet.id}")
            }
        }
    }

    /**
     * Deposit an amount of currency to the target player's wallet, send error
     * messages to the command sender. If the {@param from} is null will be considered
     * a server sent deposit.
     *
     * @param sender        the command sender.
     * @param player        the target player of the wallet.
     * @param from          the nullable sender of the currency.
     * @param currency      the selected currency.
     * @param amount        the amount of currency.
     *
     * @return              the transaction of the deposit.
     */
    fun deposit(sender: CommandSender?, player: Player, from: UUID?, currency: Currency, amount: Double): Optional<Transaction> {
        val opt = getById(player.uniqueId.toString())
        if (opt.isEmpty) {
            if (sender != null) messenger.sendMessage(sender, "economy.error.failed_finding_wallet")
            logging.warn(format("%s(%s) wallet was not found.", player.uniqueId.toString(), player.name))
            return Optional.empty()
        }

        val wallet = opt.get()
        val returned = wallet.deposit(from, currency.id, amount)
        if (returned == null) {
            if (sender != null) messenger.sendMessage(sender, "economy.deposit.sender_failed")
            logging.warn(format("Failed to deposit in wallet %s(%s)", player.uniqueId.toString(), player.name))
            return Optional.empty()
        }

        if (exists(wallet.id)) update(wallet.id, wallet)
        if (sender != null) messenger.sendMessage(sender, "economy.deposit.sender_success", currency.getSymbolAmount(amount), player.name)
        messenger.sendMessage(player, "economy.deposit.target.success", currency.getSymbolAmount(amount))

        framework.runTaskAsync {
            service.update(wallet)
            transactionService.insert(returned)
        }
        return Optional.of(returned)
    }

    /**
     * Withdraw an amount of currency from the target player's wallet, send error
     * messages to the command sender. If the {@param to} is null will be considered
     * a server sent withdrawal.
     *
     * @param sender        the command sender.
     * @param player        the target player.
     * @param to            the receiver of the currency.
     * @param currency      the selected currency.
     * @param amount        the amount of currency.
     *
     * @return              the transaction of the withdrawal.
     */
    fun withdraw(sender: CommandSender?, player: Player, to: UUID?, currency: Currency, amount: Double): Optional<Transaction> {
        val opt = getById(player.uniqueId.toString())
        if (opt.isEmpty) {
            if (sender != null) messenger.sendMessage(sender, "economy.error.failed_finding_wallet")
            logging.warn(format("%s(%s) wallet was not found.", player.uniqueId.toString(), player.name))
            return Optional.empty()
        }

        val wallet = opt.get()
        val returned = wallet.withdraw(to, currency.id, amount)
        if (returned == null) {
            if (sender != null) messenger.sendMessage(sender, "economy.withdraw.sender_failed")
            logging.warn(format("Failed to withdraw from wallet %s(%s)", player.uniqueId.toString(), player.name))
            return Optional.empty()
        }

        if (exists(wallet.id)) update(wallet.id, wallet)
        if (sender != null) messenger.sendMessage(sender, "economy.withdraw.sender_success", currency.getSymbolAmount(amount), player.name)
        messenger.sendMessage(player, "economy.withdraw.target_success", currency.getSymbolAmount(amount))
        framework.runTaskAsync {
            service.update(wallet)
            transactionService.update(returned)
        }
        return Optional.of(returned)
    }

    /**
     * Return the balance of the currency selected under the target player's wallet.
     * Sending error messages to the command sender.
     *
     * @param sender    the command sender.
     * @param player    the target player.
     * @param currency  the currency selected.
     *
     * @return          the balance of the currency selected under the target wallet.
     */
    fun balance(sender: CommandSender?, player: Player, currency: Currency): Optional<Double> {
        val opt = getById(player.uniqueId.toString())
        if (opt.isEmpty) {
            if (sender != null) messenger.sendMessage(sender, "economy.error.failed_finding_wallet")
            logging.warn(format("%s(%s) wallet was not found.", player.uniqueId.toString(), player.name))
            return Optional.empty()
        }

        val wallet = opt.get()
        val balance = wallet.getBalance(currency.id)
        if (sender != null) messenger.sendMessage(sender, "economy.balance", player.name, currency.getSymbolAmount(balance))
        return Optional.of(balance)
    }

    /**
     * Check the balance of a target player against the currency & amount selected,
     * sending error messages to the command sender.
     *
     * @param sender    the command sender.
     * @param player    the target player.
     * @param currency  the currency selected.
     * @param amount    the amount to be checked.
     *
     * @return          if the player has sufficient funds return true, if they don't return false.
     */
    fun hasBalance(sender: CommandSender?, player: Player, currency: Currency, amount: Double): Boolean {
        val opt = getById(player.uniqueId.toString())
        if (opt.isEmpty) {
            if (sender != null) messenger.sendMessage(sender, "failed_finding_wallet")
            logging.warn(format("%s(%s) wallet was not found.", player.uniqueId.toString(), player.name))
            return false
        }

        val wallet = opt.get()
        return wallet.checkBalance(currency.id, amount)
    }
}