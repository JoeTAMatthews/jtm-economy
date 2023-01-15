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
    @Permission(["erate.admin"])
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
    @Permission(["erate.admin"])
    @Usage("/erate add <from> <to> <rate>")
    fun onExchangeRateAdd(player: Player, from: Currency, to: Currency, rate: Double) {
        if (exchangeRateCache.existsBySymbol("${from.abbreviation}${to.abbreviation}")) {
            localeMessenger.sendMessage(player, "rate.found")
            return
        }

        if (from.id == to.id) {
            localeMessenger.sendMessage(player, "rate.commands.same_currency")
            return
        }

        val erate = ExchangeRate(currency_from = from.id, currency_to = to.id, symbol = "${from.abbreviation}${to.abbreviation}", rate = rate)
        val inserted = exchangeRateCache.insert(erate.id, erate)
        inserted.ifPresent { returned ->
            framework.runTaskAsync { exchangeRateService.insert(returned) }
            localeMessenger.sendMessage(player, "rate.commands.add", returned.symbol)
        }
    }

    /**
     * Remove and exchange rate.
     *
     * @param player        the command sender.
     * @param pair        the exchange rate pair.
     */
    @Command("erate")
    @SubCommand("remove")
    @Permission(["erate.admin"])
    @Usage("/erate remove <pair>")
    fun onExchangeRateRemove(player: Player, pair: String) {
        if (!exchangeRateCache.existsBySymbol(pair)) {
            localeMessenger.sendMessage(player, "rate.not_found")
            return
        }

        val returned = exchangeRateCache.getBySymbol(player, pair)
        returned.ifPresent { rate ->
            val deleted = exchangeRateCache.remove(rate.id)
            deleted.ifPresent { removed ->
                framework.runTaskAsync { exchangeRateService.delete(removed.id) }
                localeMessenger.sendMessage(player, "rate.commands.remove", removed.symbol)
            }
        }
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
    @Permission(["erate.admin"])
    @Usage("/erate update <pair> <setting> <value>")
    fun onExchangeRateUpdate(player: Player, pair: String, setting: ExchangeRateSetting, value: String) {
        if (!exchangeRateCache.existsBySymbol(pair)) {
            localeMessenger.sendMessage(player, "rate.not_found")
            return
        }

        val returned = exchangeRateCache.getBySymbol(player, pair)

        returned.ifPresent { returnedRate ->
            when(setting) {
                ExchangeRateSetting.RATE -> {
                    try {
                        val rate = value.toDouble()
                        val updated = exchangeRateCache.update(returnedRate.id, returnedRate.updateRate(rate))
                        updated.ifPresent { returned ->
                            framework.runTaskAsync { exchangeRateService.update(returned) }
                            localeMessenger.sendMessage(player, "rate.commands.updated.rate", value)
                        }
                    } catch (ex: NumberFormatException) {
                        player.sendMessage(UtilString.colour("&4Error: &cNot a number."))
                    }
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
    @Permission(["erate.admin"])
    @Usage("/erate info <symbol>")
    fun onExchangeRateInfo(player: Player, pair: String) {
        if (!exchangeRateCache.existsBySymbol(pair)) {
            localeMessenger.sendMessage(player, "rate.not_found")
            return
        }

        val rate = exchangeRateCache.getBySymbol(player, pair)
        rate.ifPresent { returned -> player.sendMessage(UtilString.colour(returned.info())) }
    }

    /**
     * Lists all the exchange rates.
     *
     * @param player        the command sender.
     */
    @Command("erate")
    @SubCommand("list")
    @Permission(["erate.admin"])
    @Usage("/erate list")
    fun onExchangeRateList(player: Player) {
        val builder = StringBuilder()
        builder.append("&b&m                        ")
        exchangeRateCache.getAll().forEach { builder.append("\n${it.info()}") }
        builder.append("\n&b&m                        ")
        player.sendMessage(UtilString.colour(builder.toString()))
    }
}