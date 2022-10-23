package com.jtmnetwork.economy.entrypoint.api

import com.jtmnetwork.economy.core.domain.entity.Currency
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface UserAPI {

    /**
     * Allow players to send currency to each other & return a boolean based on
     * if the transactions is successful.
     *
     * @param sender        the command sender.
     * @param target        the target online player.
     * @param currency      the currency selected.
     * @param amount        the amount of currency to be sent.
     *
     * @return              returns true if the transaction was successful, false if not.
     */
    fun pay(sender: Player, target: Player, currency: Currency, amount: Double): Boolean

    /**
     * Allow players to send currency to each other & return a boolean based on
     * if the transactions is successful.
     *
     * @param sender        the command sender.
     * @param target        the target offline player.
     * @param currency      the currency selected.
     * @param amount        the amount of currency to be sent.
     *
     * @return              returns true if the transaction was successful, false if not.
     */
    fun pay(sender: Player, target: OfflinePlayer, currency: Currency, amount: Double): Boolean

    /**
     * Rollbacks the target player's wallet back to a {@param transactionId} at a certain
     * point in time.
     *
     * @param sender        the command sender.
     * @param player        the target online player.
     * @param transactionId the id of the transaction the rollback will finish at.
     *
     * @return              returns true if the operation was successful, false if not.
     */
    fun startRollback(sender: CommandSender, player: Player, transactionId: Int): Boolean

    /**
     * Rollbacks the target player's wallet back to a {@param transactionId} at a certain
     * point in time.
     *
     * @param sender        the command sender.
     * @param player        the target offline player.
     * @param transactionId the id of the transaction the rollback will finish at.
     *
     * @return              returns true if the operation was successful, false if not.
     */
    fun startRollback(sender: CommandSender, player: OfflinePlayer, transactionId: Int): Boolean
}