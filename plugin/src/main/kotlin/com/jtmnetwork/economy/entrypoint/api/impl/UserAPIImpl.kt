package com.jtmnetwork.economy.entrypoint.api.impl

import com.google.inject.Inject
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.TransactionService
import com.jtmnetwork.economy.entrypoint.api.UserAPI
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UserAPIImpl @Inject constructor(private val cache: WalletCache, private val service: TransactionService): UserAPI {

    override fun pay(sender: Player, target: Player, currency: Currency, amount: Double): Boolean {
        return cache.pay(sender, target, currency, amount)
    }

    override fun pay(sender: Player, target: OfflinePlayer, currency: Currency, amount: Double): Boolean {
        return cache.pay(sender, target, currency, amount)
    }

    override fun startRollback(sender: CommandSender, player: Player, transactionId: Int) {
        val stack = service.transactionsToStack(sender, player)
        cache.rollback(sender, player, stack, transactionId)
    }

    override fun startRollback(sender: CommandSender, player: OfflinePlayer, transactionId: Int) {
        val stack = service.transactionsToStack(sender, player)
        cache.rollback(sender, player, stack, transactionId)
    }
}