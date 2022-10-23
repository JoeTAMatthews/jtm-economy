package com.jtmnetwork.economy.entrypoint.api.impl

import com.google.inject.Inject
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.data.service.TransactionService
import com.jtmnetwork.economy.entrypoint.api.TransactionAPI
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TransactionAPIImpl @Inject constructor(private val service: TransactionService): TransactionAPI {

    override fun getIngoingTransactions(sender: CommandSender?, player: Player): List<Transaction> {
        return service.getIngoingTransactions(sender, player)
    }

    override fun getIngoingTransactions(sender: CommandSender?, player: Player, currency: Currency): List<Transaction> {
        return service.getIngoingTransactions(sender, player, currency)
    }

    override fun getIngoingTransactions(sender: CommandSender?, player: OfflinePlayer): List<Transaction> {
        return service.getIngoingTransactions(sender, player)
    }

    override fun getIngoingTransactions(sender: CommandSender?, player: OfflinePlayer, currency: Currency): List<Transaction> {
        return service.getIngoingTransactions(sender, player, currency)
    }

    override fun getOutgoingTransactions(sender: CommandSender?, player: Player): List<Transaction> {
        return service.getOutgoingTransactions(sender, player)
    }

    override fun getOutgoingTransactions(sender: CommandSender?, player: Player, currency: Currency): List<Transaction> {
        return service.getOutgoingTransactions(sender, player, currency)
    }

    override fun getOutgoingTransactions(sender: CommandSender?, player: OfflinePlayer): List<Transaction> {
        return service.getOutgoingTransactions(sender, player)
    }

    override fun getOutgoingTransactions(sender: CommandSender?, player: OfflinePlayer, currency: Currency): List<Transaction> {
        return service.getOutgoingTransactions(sender, player, currency)
    }

    override fun getAllTransactions(sender: CommandSender?, player: Player): List<Transaction> {
        return service.getAllTransactions(sender, player)
    }

    override fun getAllTransactions(sender: CommandSender?, player: Player, currency: Currency): List<Transaction> {
        return service.getAllTransactions(sender, player, currency)
    }

    override fun getAllTransactions(sender: CommandSender?, player: OfflinePlayer): List<Transaction> {
        return service.getAllTransactions(sender, player)
    }

    override fun getAllTransactions(sender: CommandSender?, player: OfflinePlayer, currency: Currency): List<Transaction> {
        return service.getAllTransactions(sender, player, currency)
    }
}