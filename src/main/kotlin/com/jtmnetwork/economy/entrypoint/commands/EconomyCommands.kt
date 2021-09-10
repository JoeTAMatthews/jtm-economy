package com.jtmnetwork.economy.entrypoint.commands

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.domain.annotations.Command
import com.jtm.framework.core.domain.annotations.Permission
import com.jtm.framework.core.domain.annotations.SubCommand
import com.jtm.framework.core.domain.annotations.Usage
import com.jtm.framework.core.util.UtilString
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

@Singleton
class EconomyCommands @Inject constructor(private val framework: Framework, private val economyAPI: EconomyAPI, private val localeMessenger: LocaleMessenger) {

    @Command("econ")
    @Permission("econ.admin")
    fun onEcon(player: Player) {
        val builder = StringBuilder()
        builder.append("&b&m                               ")
        builder.append("\n&eEconomy Commands")
        builder.append("\n&f- &c/econ deposit <player> <currency> <amount>")
        builder.append("\n&f- &c/econ withdraw <player> <currency> <amount>")
        builder.append("\n&f- &c/econ balance <player> <currency>")
        builder.append("\n&b&m                               ")

        player.sendMessage(UtilString.colour(builder.toString()))
    }

    @Command("econ")
    @SubCommand("deposit")
    @Usage("/econ deposit <player> <currency> <amount>")
    @Permission("econ.admin")
    fun onEconDeposit(player: Player, target: OfflinePlayer, currency: Currency, amount: Double) {
        when (target.isOnline) {
            true -> {
                val targetPlayer = target.player
                if (targetPlayer == null) {
                    player.sendMessage(UtilString.colour("&4Error: &cTarget player not online."))
                    return
                }

                if (economyAPI.deposit(targetPlayer, currency.id, player.uniqueId, amount)) {
                    localeMessenger.sendMessage(player, "economy.deposited.sender_success", currency.getAbbreviationAmount(amount), target.name)
                    localeMessenger.sendMessage(targetPlayer, "economy.deposited.target_success", currency.getAbbreviationAmount(amount))
                } else localeMessenger.sendMessage(player, "economy.deposited.sender_failed")
            }

            false -> {
                framework.runTaskAsync {
                    if (economyAPI.deposit(target, currency.id, player.uniqueId, amount))
                        localeMessenger.sendMessage(player, "economy.deposited.sender_success", currency.getAbbreviationAmount(amount), target.name)
                    else
                        localeMessenger.sendMessage(player, "economy.deposited.sender_failed")
                }
            }
        }
    }

    @Command("econ")
    @SubCommand("withdraw")
    @Usage("/econ withdraw <player> <currency> <amount>")
    @Permission("econ.admin")
    fun onEconWithdraw(player: Player, target: OfflinePlayer, currency: Currency, amount: Double) {
        when(target.isOnline) {
            true -> {
                val targetPlayer = target.player
                if (targetPlayer == null) {
                    player.sendMessage(UtilString.colour("&4Error: &cTarget player not online."))
                    return
                }

                if (economyAPI.withdraw(targetPlayer, currency.id, player.uniqueId, amount)) {
                    localeMessenger.sendMessage(player, "economy.withdraw.sender_success", currency.getAbbreviationAmount(amount), target.name)
                    localeMessenger.sendMessage(targetPlayer, "economy.withdraw.target_success", currency.getAbbreviationAmount(amount))
                } else localeMessenger.sendMessage(player, "economy.withdraw.sender_failed")
            }

            false -> {
                framework.runTaskAsync {
                    if (economyAPI.withdraw(target, currency.id, player.uniqueId, amount))
                        localeMessenger.sendMessage(player, "economy.withdraw.sender_success", currency.getAbbreviationAmount(amount), target.name)
                    else
                        localeMessenger.sendMessage(player, "economy.withdraw.sender_failed")
                }
            }
        }
    }

    @Command("econ")
    @SubCommand("balance")
    @Usage("/econ balance <player> <currency>")
    @Permission("econ.admin")
    fun onEconBalance(player: Player, target: OfflinePlayer, currency: Currency) {
        when(target.isOnline) {
            true -> {
                val targetPlayer = target.player
                if (targetPlayer == null) {
                    player.sendMessage(UtilString.colour("&4Error: &cTarget player not online."))
                    return
                }

                val balance = economyAPI.balance(targetPlayer, currency.id) ?: 0.0
                localeMessenger.sendMessage(player, "economy.balance", target.name, currency.getAbbreviationAmount(balance))
            }

            false -> {
                framework.runTaskAsync {
                    val balance = economyAPI.balance(target, currency.id) ?: 0.0
                    localeMessenger.sendMessage(player, "economy.balance", target.name, currency.getAbbreviationAmount(balance))
                }
            }
        }
    }
}