package com.jtmnetwork.economy.entrypoint.commands

import com.google.inject.Inject
import com.jtm.framework.Framework
import com.jtm.framework.core.domain.annotations.Command
import com.jtm.framework.core.domain.annotations.Usage
import com.jtm.framework.core.util.UtilString
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class PayCommands @Inject constructor(private val framework: Framework, private val economyAPI: EconomyAPI, private val localeMessenger: LocaleMessenger) {

    // TODO: Add messages to the en.yml
    @Command("pay")
    @Usage("/pay <player> <currency> <amount>")
    fun onPay(player: Player, target: OfflinePlayer, currency: Currency, amount: Double) {
        when(target.isOnline) {
            true -> {
                val targetPlayer = target.player
                if (targetPlayer == null) {
                    player.sendMessage(UtilString.colour("&4Error: &cTarget player not online."))
                    return
                }

                if (economyAPI.pay(targetPlayer, player, currency.id, amount)) {
                    localeMessenger.sendMessage(player, "pay.sender_success", targetPlayer.name, currency.getAbbreviationAmount(amount))
                    localeMessenger.sendMessage(targetPlayer, "pay.receiver_success", player.name, currency.getAbbreviationAmount(amount))
                } else localeMessenger.sendMessage(player, "pay.sender_failed")
            }

            false -> {
                framework.runTaskAsync {
                    if (economyAPI.pay(target, player, currency.id, amount))
                        localeMessenger.sendMessage(player, "pay.sender_success", target.name, currency.getAbbreviationAmount(amount))
                    else
                        localeMessenger.sendMessage(player, "pay.sender_failed")
                }
            }
        }
    }
}