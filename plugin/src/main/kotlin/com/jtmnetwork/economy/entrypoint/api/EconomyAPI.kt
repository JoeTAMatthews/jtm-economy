package com.jtmnetwork.economy.entrypoint.api

import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.entity.Wallet
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

interface EconomyAPI {

    /**
     * Exchange a currency for another based on a set amount.
     *
     * @param player        the target player wallet
     * @param from          the currency the amount is being exchanged from
     * @param to            the currency the amount is being exchanged to
     * @param amount        the currency amount
     *
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
     *
     * @return              if successful return true, if unsuccessful return false
     */
    fun exchangeAmountOffline(player: OfflinePlayer, from: UUID, to: UUID, amount: Double): Boolean

    /**
     * Return the wallet from a target player
     *
     * @param player        the target player
     *
     * @return              the wallet of the targeted player.
     */
    fun getWallet(player: Player): Optional<Wallet>

    /**
     * Return the wallet from a target player
     *
     * @param player        the target player
     *
     * @return              the wallet of the targeted player.
     */
    fun getWallet(player: OfflinePlayer): Optional<Wallet>

    /**
     * Return the global standard currency for the server.
     *
     * @return              the currency that has been set.
     */
    fun getGlobalCurrency(): Optional<Currency>

    /**
     * Return the currency from an identifier
     *
     * @param id            the unique identifier
     *
     * @return              the currency found.
     */
    fun getCurrency(id: UUID): Optional<Currency>

    /**
     * Return the currency from a name
     *
     * @param name          the name of the currency
     *
     * @return              the currency found.
     */
    fun getCurrency(name: String): Optional<Currency>

    /**
     * List all currencies available.
     *
     * @return              the list of currencies created.
     */
    fun getCurrencies(): List<Currency>

    /**
     * Start a rollback operation for the online player from previous transactions, and stopping
     * at a set transaction index.
     *
     * @param initiator     the initiator of the operation using a command.
     * @param target        the target of the rollback operation.
     * @param transactionId the index where the operation will stop.
     *
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
     *
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
     *
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
     *
     * @return              if successful return true, if unsuccessful return false
     */
    fun pay(target: OfflinePlayer, sender: Player, currencyId: UUID, amount: Double): Boolean
}