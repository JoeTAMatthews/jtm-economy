package com.jtmnetwork.economy.entrypoint.commands

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.core.domain.annotations.Command
import com.jtm.framework.core.domain.annotations.Permission
import com.jtm.framework.core.domain.annotations.SubCommand
import com.jtm.framework.core.domain.annotations.Usage
import com.jtm.framework.core.util.UtilString
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

@Singleton
class EconomyCommands @Inject constructor(private val economyAPI: EconomyAPI) {

    @Command("econ")
    @Permission("econ.admin")
    fun onEcon(player: Player) {
        val builder = StringBuilder()
        builder.append("&b&m                               ")
        builder.append("\n&eEconomy Commands")
        builder.append("\n&f- &c/econ deposit <player> <currency> <amount>")
        builder.append("\n&f- &c/econ withdraw <player> <currency> <amount>")
        builder.append("\n&f- &c/econ balance <player> <currency>")
        builder.append("&b&m                               ")

        player.sendMessage(UtilString.colour(builder.toString()))
    }

    @Command("econ")
    @SubCommand("deposit")
    @Usage("/econ deposit <player> <currency> <amount>")
    @Permission("econ.admin")
    fun onEconDeposit(player: Player, offlinePlayer: OfflinePlayer, currency: Currency, amount: Double) {

    }

    @Command("econ")
    @SubCommand("withdraw")
    @Usage("/econ withdraw <player> <currency> <amount>")
    @Permission("econ.admin")
    fun onEconWithdraw(player: Player, offlinePlayer: OfflinePlayer, currency: Currency, amount: Double) {

    }

    @Command("econ")
    @SubCommand("balance")
    @Usage("/econ balance <player> <currency>")
    @Permission("econ.admin")
    fun onEconBalance(player: Player, offlinePlayer: OfflinePlayer, currency: Currency) {

    }
}