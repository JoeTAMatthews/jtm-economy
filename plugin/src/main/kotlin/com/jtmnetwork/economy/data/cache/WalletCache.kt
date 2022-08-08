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
     * Check the balance of an online player against an amount, ensuring that the player has the sufficient funds.
     *
     * @param player        the target online player
     * @param currency      the currency to be used.
     * @param amount        the currency amount.
     * @return              if the player has sufficient amount return true, if they don't return false
     */
    fun hasBalance(player: Player, currency: UUID, amount: Double): Boolean {
        val opt = getById(player.uniqueId.toString())
        if (opt.isEmpty) return false
        val wallet = opt.get()
        val balance = wallet.balances[currency] ?: return false
        return balance >= amount
    }

    /**
     * Check the balance of an offline player against an amount, ensuring that the player has the sufficient funds.
     *
     * @param player        the target offline player
     * @param currency      the currency to be used.
     * @param amount        the currency amount.
     * @return              if the player has sufficient amount return true, if they don't return false
     */
    fun hasBalanceOffline(player: OfflinePlayer, currency: UUID, amount: Double): Boolean {
        val opt = service.get(player.uniqueId.toString())
        if (opt.isEmpty) return false
        val wallet = opt.get()
        val balance = wallet.balances[currency] ?: return false
        return balance >= amount
    }

    /**
     * Deposit a currency amount to a target wallet.
     *
     * @param sender        the sender of the currency.
     * @param wallet        the target wallet.
     * @param currency      the currency selected.
     * @param amount        the amount of currency.
     * @return              the transaction of the deposit.
     */
    fun deposit(sender: UUID?, wallet: Wallet, currency: UUID, amount: Double): Transaction? {
        val current = wallet.getBalance(currency)
        val deposited = wallet.addBalance(currency, amount) ?: return null
        if (exists(wallet.id)) update(wallet.id, wallet)
        framework.runTaskAsync { service.update(deposited) }
        return Transaction(type = TransactionType.IN, sender = sender, receiver = UUID.fromString(wallet.id), currency = currency, amount = amount, previous_balance = current, new_balance = deposited.getBalance(currency))
    }

    /**
     * Withdraw a currency amount from a target wallet.
     *
     * @param sender        the sender of the currency.
     * @param wallet        the target wallet
     * @param currency      the currency selected.
     * @param amount        the amount of currency.
     * @return              the transaction of the withdrawal.
     */
    fun withdraw(sender: UUID?, wallet: Wallet, currency: UUID, amount: Double): Transaction? {
        val current = wallet.getBalance(currency)
        val withdrew = wallet.removeBalance(currency, amount) ?: return null
        if (exists(wallet.id)) update(wallet.id, wallet)
        framework.runTaskAsync { service.update(withdrew) }
        return Transaction(type = TransactionType.OUT, sender = UUID.fromString(wallet.id), receiver = sender, currency = currency, amount = amount, previous_balance = current, new_balance = withdrew.getBalance(currency))
    }
}