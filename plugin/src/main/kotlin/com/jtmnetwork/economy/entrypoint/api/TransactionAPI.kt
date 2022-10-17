package com.jtmnetwork.economy.entrypoint.api

import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

interface TransactionAPI {

    /**
     * Deposit currency amount to an online player.
     *
     * @param sender        the initiator of the deposit.
     * @param player        the receiver of the deposit
     * @param from          the sender of the currency, if null will be registered as the server.
     * @param currency      the currency object.
     * @param amount        the amount of the currency to be deposited
     *
     * @return              if completed return true, if failed return false
     */
    fun deposit(sender: CommandSender?, player: Player, from: UUID?, currency: Currency, amount: Double): Optional<Transaction>

    /**
     * Deposit currency amount to an offline player.
     *
     * @param sender        the initiator of the deposit.
     * @param player        the receiver of the deposit.
     * @param from          the sender of the currency, if null will be registered as the server.
     * @param currency      the currency object.
     * @param amount        the amount of the currency to be deposited
     *
     * @return              if completed return true, if failed return false
     */
    fun deposit(sender: CommandSender?, player: OfflinePlayer, from: UUID?, currency: Currency, amount: Double): Optional<Transaction>

    /**
     * Withdraw currency amount from an online player.
     *
     * @param sender        the initiator of the withdrawal.
     * @param player        the receiver of the withdrawal
     * @param to            the receiver of the currency, if null will be registered as the server.
     * @param currency      the currency object.
     * @param amount        the amount of the currency to be withdrawn.
     *
     * @return              if completed return true, if failed return false
     */
    fun withdraw(sender: CommandSender?, player: Player, to: UUID?, currency: Currency, amount: Double): Optional<Transaction>

    /**
     * Withdraw currency amount from an offline player.
     *
     * @param sender        the initiator of the withdrawal.
     * @param player        the receiver of the withdrawal
     * @param to            the receiver of the currency, if null will be registered as the server.
     * @param currency      the currency object.
     * @param amount        the amount of the currency to be withdrawn.
     *
     * @return              if completed return true, if failed return false
     */
    fun withdraw(sender: CommandSender?, player: OfflinePlayer, to: UUID?, currency: Currency, amount: Double): Optional<Transaction>
}