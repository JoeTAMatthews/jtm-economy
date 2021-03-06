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

    /**
     * Show all commands under "/erate"
     *
     * @param player        the command sender.
     */
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

    /**
     * Add an exchange rate for 2 currencies.
     *
     * @param player        the command sender.
     * @param from          the currency to be exchanged from.
     * @param to            the currency to be exchange to.
     * @param rate          the rate to be used when exchanging.
     */
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

    /**
     * Remove and exchange rate.
     *
     * @param player        the command sender.
     * @param pair        the exchange rate pair.
     */
    @Command("erate")
    @SubCommand("remove")
    @Permission("erate.admin")
    @Usage("/erate remove <pair>")
    fun onExchangeRateRemove(player: Player, pair: String) {
        if (!exchangeRateCache.existsBySymbol(pair)) {
            localeMessenger.sendMessage(player, "exchangerate.not_found")
            return
        }

        val returned = exchangeRateCache.getBySymbol(pair) ?: return
        val deleted = exchangeRateCache.remove(returned.id) ?: return
        framework.runTaskAsync { exchangeRateService.delete(deleted.id) }
        localeMessenger.sendMessage(player, "exchangerate.commands.remove", deleted.symbol)
    }

    /**
     * Update the exchange rate of a pair.
     *
     * @param player        the command sender.
     * @param pair          the pair of the exchange rate.
     * @param setting       the exchange rate setting to be updated.
     * @param value         the value to be updated with.
     */
    @Command("erate")
    @SubCommand("update")
    @Permission("erate.admin")
    @Usage("/erate update <pair> <setting> <value>")
    fun onExchangeRateUpdate(player: Player, pair: String, setting: ExchangeRateSetting, value: String) {
        if (!exchangeRateCache.existsBySymbol(pair)) {
            localeMessenger.sendMessage(player, "exchangerate.not_found")
            return
        }

        val returned = exchangeRateCache.getBySymbol(pair) ?: return

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

    /**
     * Show information about an exchange rate.
     *
     * @param player        the command sender.
     * @param pair          the pair of the exchange rate.
     */
    @Command("erate")
    @SubCommand("info")
    @Permission("erate.admin")
    @Usage("/erate info <symbol>")
    fun onExchangeRateInfo(player: Player, pair: String) {
        if (!exchangeRateCache.existsBySymbol(pair)) {
            localeMessenger.sendMessage(player, "exchangerate.not_found")
            return
        }

        val rate = exchangeRateCache.getBySymbol(pair) ?: return
        player.sendMessage(UtilString.colour(rate.info()))
    }

    /**
     * Lists all the exchange rates.
     *
     * @param player        the command sender.
     */
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