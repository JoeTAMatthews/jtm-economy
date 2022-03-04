package com.jtmnetwork.economy.entrypoint.api

import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.entity.Wallet
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

interface EconomyAPI {

    fun deposit(player: Player, currency: UUID, from: UUID?, amount: Double): Boolean

    fun deposit(player: OfflinePlayer, currency: UUID, from: UUID?, amount: Double): Boolean

    fun withdraw(player: Player, currency: UUID, from: UUID?, amount: Double): Boolean

    fun withdraw(player: OfflinePlayer, currency: UUID, from: UUID?, amount: Double): Boolean

    fun balance(player: Player, currency: UUID): Double?

    fun balance(player: OfflinePlayer, currency: UUID): Double?

    fun getTransactions(player: Player, currency: UUID): List<Transaction>

    fun getTransactions(player: OfflinePlayer, currency: UUID): List<Transaction>

    fun getTransactions(player: Player): List<Transaction>

    fun getTransactions(player: OfflinePlayer): List<Transaction>

    fun exchangeAmount(player: Player, from: UUID, to: UUID, amount: Double): Boolean

    fun exchangeAmount(player: OfflinePlayer, from: UUID, to: UUID, amount: Double): Boolean

    fun getWallet(player: Player): Wallet?

    fun getWallet(player: OfflinePlayer): Wallet?

    fun getCurrency(id: UUID): Currency?

    fun getCurrency(name: String): Currency?

    fun processRollback(initiator: Player, target: Player, transactionId: Int): Boolean

    fun processRollback(initiator: Player, target: OfflinePlayer, transactionId: Int): Boolean

    fun pay(target: Player, sender: Player, currencyId: UUID, amount: Double): Boolean

    fun pay(target: OfflinePlayer, sender: Player, currencyId: UUID, amount: Double): Boolean
}