package com.jtmnetwork.economy.entrypoint.api

import com.google.inject.Inject
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.WalletService
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class TransactionAPIImpl @Inject constructor(private val cache: WalletCache, private val service: WalletService): TransactionAPI {

    override fun deposit(sender: CommandSender, player: Player, from: UUID?, currency: Currency, amount: Double): Optional<Transaction> {
        return cache.deposit(sender, player, from, currency, amount)
    }

    override fun deposit(sender: CommandSender, player: OfflinePlayer, from: UUID?, currency: Currency, amount: Double): Optional<Transaction> {
        return service.deposit(sender, player, from, currency, amount)
    }

    override fun withdraw(sender: CommandSender, player: Player, to: UUID?, currency: Currency, amount: Double): Optional<Transaction> {
        return cache.withdraw(sender, player, to, currency, amount)
    }

    override fun withdraw(sender: CommandSender, player: OfflinePlayer, to: UUID?, currency: Currency, amount: Double): Optional<Transaction> {
        return service.withdraw(sender, player, to, currency, amount)
    }
}