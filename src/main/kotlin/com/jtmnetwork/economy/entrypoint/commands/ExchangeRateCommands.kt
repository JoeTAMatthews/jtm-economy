package com.jtmnetwork.economy.entrypoint.commands

import com.google.inject.Inject
import com.jtm.framework.Framework
import com.jtm.framework.core.domain.annotations.Command
import com.jtm.framework.core.domain.annotations.Permission
import com.jtm.framework.core.domain.annotations.SubCommand
import com.jtm.framework.core.domain.annotations.Usage
import com.jtm.framework.core.util.UtilString
import com.jtmnetwork.economy.core.domain.constants.ExchangeRateSetting
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.ExchangeRate
import com.jtmnetwork.economy.data.cache.ExchangeRateCache
import com.jtmnetwork.economy.data.service.ExchangeRateService
import org.bukkit.entity.Player
import java.lang.NumberFormatException

class ExchangeRateCommands @Inject constructor(private val framework: Framework, private val exchangeRateCache: ExchangeRateCache, private val exchangeRateService: ExchangeRateService) {

    private val localeMessenger = framework.getLocaleMessenger()

    @Command("erate")
    @Permission("erate.admin")
    fun onExchangeRate(player: Player) {
        val builder = StringBuilder()
        builder.append("&b&m                       ")
        builder.append("\n&eExchange Rate Commands")
        builder.append("\n&f- &c/erate add <from> <to> <value>")
        builder.append("\n&f- &c/erate remove <symbol>")
        builder.append("\n&f- &c/erate update <symbol> <setting> <value>")
        builder.append("\n&f- &c/erate info <symbol>")
        builder.append("\n&f- &c/erate list")
        builder.append("\n&b&m                       ")

        player.sendMessage(UtilString.colour(builder.toString()))
    }

    @Command("erate")
    @SubCommand("add")
    @Permission("erate.admin")
    @Usage("/erate add <from> <to> <rate>")
    fun onExchangeRateAdd(player: Player, from: Currency, to: Currency, rate: Double) {
        if (exchangeRateCache.existsBySymbol("${from.abbreviation}${to.abbreviation}")) {
            localeMessenger.sendMessage(player, "exchangerate.found")
            return
        }

        if (from.id == to.id) {
            localeMessenger.sendMessage(player, "exchangerate.commands.same_currency")
            return
        }

        val exchangeRate = ExchangeRate(currency_from = from.id, currency_to = to.id, symbol = "${from.abbreviation}${to.abbreviation}", rate = rate)
        val inserted = exchangeRateCache.insert(exchangeRate.id, exchangeRate) ?: return
        framework.runTaskAsync { exchangeRateService.insert(inserted) }
        localeMessenger.sendMessage(player, "exchangerate.commands.add", inserted.symbol)
    }

    @Command("erate")
    @SubCommand("remove")
    @Permission("erate.admin")
    @Usage("/erate remove <symbol>")
    fun onExchangeRateRemove(player: Player, symbol: String) {
        if (!exchangeRateCache.existsBySymbol(symbol)) {
            localeMessenger.sendMessage(player, "exchangerate.not_found")
            return
        }

        val returned = exchangeRateCache.getBySymbol(symbol) ?: return
        val deleted = exchangeRateCache.remove(returned.id) ?: return
        framework.runTaskAsync { exchangeRateService.delete(deleted.id) }
        localeMessenger.sendMessage(player, "exchangerate.commands.remove", deleted.symbol)
    }

    @Command("erate")
    @SubCommand("update")
    @Permission("erate.admin")
    @Usage("/erate update <symbol> <setting> <value>")
    fun onExchangeRateUpdate(player: Player, symbol: String, setting: ExchangeRateSetting, value: String) {
        if (!exchangeRateCache.existsBySymbol(symbol)) {
            localeMessenger.sendMessage(player, "exchangerate.not_found")
            return
        }

        val returned = exchangeRateCache.getBySymbol(symbol) ?: return

        when(setting) {
            ExchangeRateSetting.RATE -> {
                try {
                    val rate = value.toDouble()
                    val updated = exchangeRateCache.update(returned.id, returned.updateRate(rate)) ?: return
                    framework.runTaskAsync { exchangeRateService.update(updated) }
                    localeMessenger.sendMessage(player, "exchangerate.commands.updated.rate", value)
                } catch (ex: NumberFormatException) {
                    player.sendMessage(UtilString.colour("&4Error: &cNot a number."))
                }
            }
        }
    }

    @Command("erate")
    @SubCommand("info")
    @Permission("erate.admin")
    @Usage("/erate info <symbol>")
    fun onExchangeRateInfo(player: Player, symbol: String) {
        if (!exchangeRateCache.existsBySymbol(symbol)) {
            localeMessenger.sendMessage(player, "exchangerate.not_found")
            return
        }

        val rate = exchangeRateCache.getBySymbol(symbol) ?: return
        player.sendMessage(UtilString.colour(rate.info()))
    }

    @Command("erate")
    @SubCommand("list")
    @Permission("erate.admin")
    @Usage("/erate list")
    fun onExchangeRateList(player: Player) {
        val builder = StringBuilder()
        builder.append("&b&m                        ")
        exchangeRateCache.getAll()?.forEach { builder.append("\n${it.info()}") }
        builder.append("\n&b&m                        ")
        player.sendMessage(UtilString.colour(builder.toString()))
    }
}