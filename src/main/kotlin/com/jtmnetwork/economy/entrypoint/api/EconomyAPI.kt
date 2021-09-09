package com.jtmnetwork.economy.entrypoint.api

import com.jtmnetwork.economy.core.domain.entity.Transaction
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
}