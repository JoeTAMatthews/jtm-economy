package com.jtmnetwork.economy.entrypoint.api

import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.entity.Wallet
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

interface EconomyAPI {

    /**
     * Deposit currency amount to a online player.
     *
     * @param player        the receiver of the deposit
     * @param currency      the currency unique identifier
     * @param from          the sender of the currency, if null will be registered as the server.
     * @param amount        the amount of the currency to be deposited
     * @return              if completed return true, if failed return false
     */
    fun deposit(player: Player, currency: UUID, from: UUID?, amount: Double): Boolean

    /**
     * Deposit currency amount to a offline player.
     *
     * @param player        the receiver of the deposit
     * @param currency      the currency unique identifier
     * @param from          the sender of the currency, if null will be registered as the server.
     * @param amount        the amount of the currency to be deposited
     * @return              if completed return true, if failed return false
     */
    fun deposit(player: OfflinePlayer, currency: UUID, from: UUID?, amount: Double): Boolean

    /**
     * Withdraw currency amount from a online player.
     *
     * @param player        the receiver of the withdrawal
     * @param currency      the currency unique identifier
     * @param from          the sender of the currency, if null will be registered as the server.
     * @param amount        the amount of the currency to be withdrawn.
     * @return              if completed return true, if failed return false
     */
    fun withdraw(player: Player, currency: UUID, from: UUID?, amount: Double): Boolean

    /**
     * Withdraw currency amount from a offline player.
     *
     * @param player        the receiver of the withdrawal
     * @param currency      the currency unique identifier
     * @param from          the sender of the currency, if null will be registered as the server.
     * @param amount        the amount of the currency to be withdrawn.
     * @return              if completed return true, if failed return false
     */
    fun withdraw(player: OfflinePlayer, currency: UUID, from: UUID?, amount: Double): Boolean

    /**
     * Returns the balance amount of the currency found in the online player's wallet.
     *
     * @param player        the target player wallet
     * @param currency      the currency unique identifier
     * @return              the balance found in the wallet
     */
    fun balance(player: Player, currency: UUID): Double?

    /**
     * Returns the balance amount of the currency found in the offline player's wallet.
     *
     * @param player        the target player wallet
     * @param currency      the currency unique identifier
     * @return              the balance found in the wallet
     */
    fun balance(player: OfflinePlayer, currency: UUID): Double?

    /**
     * Returns the transactions the online player has been involved in under a currency.
     *
     * @param player        the target player wallet
     * @param currency      the currency unique identifier
     * @see                 Transaction
     * @return              the list of transactions found.
     */
    fun getTransactions(player: Player, currency: UUID): List<Transaction>

    /**
     * Returns the transactions the offline player has been involved in under a currency.
     *
     * @param player        the target player wallet
     * @param currency      the currency unique identifier
     * @see                 Transaction
     * @return              the list of transactions found.
     */
    fun getTransactions(player: OfflinePlayer, currency: UUID): List<Transaction>

    /**
     * Returns all transactions found under an online player.
     *
     * @param player        the target player wallet
     * @see                 Transaction
     * @return              the list of transactions found.
     */
    fun getTransactions(player: Player): List<Transaction>

    /**
     * Returns all transactions found under an offline player.
     *
     * @param player        the target player wallet
     * @see                 Transaction
     * @return              the list of transactions found.
     */
    fun getTransactions(player: OfflinePlayer): List<Transaction>

    /**
     * Exchange a currency for another based on a set amount.
     *
     * @param player        the target player wallet
     * @param from          the currency the amount is being exchanged from
     * @param to            the currency the amount is being exchanged to
     * @param amount        the currency amount
     * @return              if successful return true, if unsuccessful return false
     */
    fun exchangeAmount(player: Player, from: UUID, to: UUID, amount: Double): Boolean

    /**
     * Exchange a currency for another based on a set amount.
     *
     * @param player        the target player wallet
     * @param from          the currency the amount is being exchanged from
     * @param to            the currency the amount is being exchanged to
     * @param amount        the currency amount
     * @return              if successful return true, if unsuccessful return false
     */
    fun exchangeAmount(player: OfflinePlayer, from: UUID, to: UUID, amount: Double): Boolean

    /**
     * Return the wallet from a target player
     *
     * @param player        the target player
     * @return              the wallet of the targeted player.
     */
    fun getWallet(player: Player): Wallet?

    /**
     * Return the wallet from a target player
     *
     * @param player        the target player
     * @return              the wallet of the targeted player.
     */
    fun getWallet(player: OfflinePlayer): Wallet?

    /**
     * Return the currency from an identifier
     *
     * @param id            the unique identifier
     * @return              the currency found.
     */
    fun getCurrency(id: UUID): Currency?

    /**
     * Return the currency from a name
     *
     * @param name          the name of the currency
     * @return              the currency found.
     */
    fun getCurrency(name: String): Currency?

    /**
     * Start a rollback operation for the online player from previous transactions, and stopping
     * at a set transaction index.
     *
     * @param initiator     the initiator of the operation using a command.
     * @param target        the target of the rollback operation.
     * @param transactionId the index where the operation will stop.
     * @return              if successful return true, if unsuccessful return false
     */
    fun processRollback(initiator: Player, target: Player, transactionId: Int): Boolean

    /**
     * Start a rollback operation for the offline player from previous transactions, and stopping
     * at a set transaction index.
     *
     * @param initiator     the initiator of the operation using a command.
     * @param target        the target of the rollback operation.
     * @param transactionId the index where the operation will stop.
     * @return              if successful return true, if unsuccessful return false
     */
    fun processRollback(initiator: Player, target: OfflinePlayer, transactionId: Int): Boolean

    /**
     * Allow player to pay another player using their selected currency.
     *
     * @param target        the receiver of the currency amount
     * @param sender        the sender of the currency amount
     * @param currencyId    the currency unique identifier
     * @param amount        the amount of currency to be sent.
     * @return              if successful return true, if unsuccessful return false
     */
    fun pay(target: Player, sender: Player, currencyId: UUID, amount: Double): Boolean

    /**
     * Allow player to pay an offline player using their selected currency.
     *
     * @param target        the offline receiver of the currency amount.
     * @param sender        the sender of the currency amount.
     * @param currencyId    the currency unique identifier.
     * @param amount        the amount of currency to be sent.
     * @return              if successful return true, if unsuccessful return false
     */
    fun pay(target: OfflinePlayer, sender: Player, currencyId: UUID, amount: Double): Boolean
}