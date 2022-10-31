package com.jtmnetwork.economy.entrypoint.api

import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface TransactionAPI {

    fun getIngoingTransactions(sender: CommandSender?, player: Player): List<Transaction>

    fun getIngoingTransactions(sender: CommandSender?, player: Player, currency: Currency): List<Transaction>

    fun getIngoingTransactions(sender: CommandSender?, player: OfflinePlayer): List<Transaction>

    fun getIngoingTransactions(sender: CommandSender?, player: OfflinePlayer, currency: Currency): List<Transaction>

    fun getOutgoingTransactions(sender: CommandSender?, player: Player): List<Transaction>

    fun getOutgoingTransactions(sender: CommandSender?, player: Player, currency: Currency): List<Transaction>

    fun getOutgoingTransactions(sender: CommandSender?, player: OfflinePlayer): List<Transaction>

    fun getOutgoingTransactions(sender: CommandSender?, player: OfflinePlayer, currency: Currency): List<Transaction>

    fun getAllTransactions(sender: CommandSender?, player: Player): List<Transaction>

    fun getAllTransactions(sender: CommandSender?, player: Player, currency: Currency): List<Transaction>

    fun getAllTransactions(sender: CommandSender?, player: OfflinePlayer): List<Transaction>

    fun getAllTransactions(sender: CommandSender?, player: OfflinePlayer, currency: Currency): List<Transaction>
}