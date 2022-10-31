package com.jtmnetwork.economy.entrypoint.api.impl

import com.google.inject.Inject
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.WalletService
import com.jtmnetwork.economy.entrypoint.api.WalletAPI
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class WalletAPIImpl @Inject constructor(private val cache: WalletCache, private val service: WalletService): WalletAPI {

    override fun deposit(sender: CommandSender?, player: Player, from: UUID?, currency: Currency, amount: Double): Optional<Transaction> {
        return cache.deposit(sender, player, from, currency, amount)
    }

    override fun deposit(sender: CommandSender?, player: OfflinePlayer, from: UUID?, currency: Currency, amount: Double): Optional<Transaction> {
        return service.deposit(sender, player, from, currency, amount)
    }

    override fun withdraw(sender: CommandSender?, player: Player, to: UUID?, currency: Currency, amount: Double): Optional<Transaction> {
        return cache.withdraw(sender, player, to, currency, amount)
    }

    override fun withdraw(sender: CommandSender?, player: OfflinePlayer, to: UUID?, currency: Currency, amount: Double): Optional<Transaction> {
        return service.withdraw(sender, player, to, currency, amount)
    }

    override fun balance(sender: CommandSender?, player: Player, currency: Currency): Optional<Double> {
        return cache.balance(sender, player, currency)
    }

    override fun balance(sender: CommandSender?, player: OfflinePlayer, currency: Currency): Optional<Double> {
        return service.balance(sender, player, currency)
    }

    override fun hasBalance(sender: CommandSender?, player: Player, currency: Currency, amount: Double): Boolean {
        return cache.hasBalance(sender, player, currency, amount)
    }

    override fun hasBalance(sender: CommandSender?, player: OfflinePlayer, currency: Currency, amount: Double): Boolean {
        return service.hasBalance(sender, player, currency, amount)
    }

    override fun getWallet(sender: CommandSender?, player: Player): Optional<Wallet> {
        return cache.getWallet(sender, player)
    }

    override fun getWallet(sender: CommandSender?, player: OfflinePlayer): Optional<Wallet> {
        return service.getWallet(sender, player)
    }
}