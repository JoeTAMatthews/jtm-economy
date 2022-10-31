package com.jtmnetwork.economy.entrypoint.commands

import com.google.inject.Inject
import com.jtm.framework.core.domain.annotations.Command
import com.jtm.framework.core.domain.annotations.Usage
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import com.jtmnetwork.economy.entrypoint.api.ExchangeAPI
import org.bukkit.entity.Player

class ExchangeCommands @Inject constructor(private val exchangeAPI: ExchangeAPI, private val localeMessenger: LocaleMessenger) {

    /**
     * Exchange currencies between each other.
     *
     * @param player        the command sender.
     * @param from          the currency to exchange from.
     * @param to            the currency to exchange to.
     * @param amount        the amount of currency to be exchanged.
     */
    @Command("exchange")
    @Usage("/exchange <from> <to> <amount>")
    fun onExchange(player: Player, from: Currency, to: Currency, amount: Double) {
        val exchange = exchangeAPI.exchange(player, from, to, amount)
        localeMessenger.sendMessage(player, if (exchange) "exchange.commands.success" else "exchange.commands.failed")
    }
}