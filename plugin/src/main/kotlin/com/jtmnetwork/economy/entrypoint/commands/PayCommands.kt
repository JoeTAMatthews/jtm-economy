package com.jtmnetwork.economy.entrypoint.commands

import com.google.inject.Inject
import com.jtm.framework.Framework
import com.jtm.framework.core.domain.annotations.Command
import com.jtm.framework.core.domain.annotations.Usage
import com.jtm.framework.core.util.UtilString
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import com.jtmnetwork.economy.entrypoint.api.UserAPI
import okhttp3.internal.format
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class PayCommands @Inject constructor(private val framework: Framework, private val userAPI: UserAPI, private val messenger: LocaleMessenger) {

    private val logging = framework.getLogging()

    /**
     * Allow players to pay each other using a selected currency.
     *
     * @param player        the command sender.
     * @param target        the target player.
     * @param currency      the selected currency.
     * @param amount        the amount to be transferred.
     */
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

                if (userAPI.pay(player, targetPlayer, currency, amount))
                    logging.info(format("%s has successfully paid %s: %s", player.name, targetPlayer.name, currency.getSymbolAmount(amount)))
                else
                    logging.info(format("%s has failed to pay %s: %s", player.name, targetPlayer.name, currency.getSymbolAmount(amount)))
            }

            false -> {
                if (userAPI.pay(player, target, currency, amount))
                    logging.info(format("%s has successfully paid %s: %s", player.name, target.name ?: target.uniqueId, currency.getSymbolAmount(amount)))
                else
                    logging.info(format("%s has failed to pay %s: %s", player.name, target.name ?: target.uniqueId, currency.getSymbolAmount(amount)))
            }
        }
    }
}