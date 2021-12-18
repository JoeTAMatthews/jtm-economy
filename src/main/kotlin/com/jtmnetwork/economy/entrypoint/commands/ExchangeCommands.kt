package com.jtmnetwork.economy.entrypoint.commands

import com.google.inject.Inject
import com.jtm.framework.core.domain.annotations.Command
import com.jtm.framework.core.domain.annotations.Usage
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import org.bukkit.entity.Player

class ExchangeCommands @Inject constructor(private val economyAPI: EconomyAPI, private val localeMessenger: LocaleMessenger) {

    @Command("exchange")
    @Usage("/exchange <from> <to> <amount>")
    fun onExchange(player: Player, from: Currency, to: Currency, amount: Double) {
        val exchange = economyAPI.exchangeAmount(player, from.id, to.id, amount)
        localeMessenger.sendMessage(player, if (exchange) "exchange.commands.success" else "exchange.commands.failed")
    }
}