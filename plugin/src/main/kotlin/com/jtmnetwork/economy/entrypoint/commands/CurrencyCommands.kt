package com.jtmnetwork.economy.entrypoint.commands

import com.google.inject.Inject
import com.jtm.framework.Framework
import com.jtm.framework.core.domain.annotations.Command
import com.jtm.framework.core.domain.annotations.Permission
import com.jtm.framework.core.domain.annotations.SubCommand
import com.jtm.framework.core.domain.annotations.Usage
import com.jtm.framework.core.util.UtilString
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.event.currency.CurrencyAddEvent
import com.jtmnetwork.economy.core.domain.event.currency.CurrencyRemoveEvent
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.service.CurrencyService
import org.bukkit.entity.Player
import java.util.*
import kotlin.math.exp

class CurrencyCommands @Inject constructor(private val framework: Framework, private val cache: CurrencyCache, private val service: CurrencyService) {

    private val localeMessenger = framework.getLocaleMessenger()

    /**
     * Shows all the commands under "/currency"
     *
     * @param player        the command sender.
     */
    @Command("currency")
    @Permission(["currency.admin"])
    fun onCurrency(player: Player) {
        val builder = StringBuilder()
        builder.append("&b&m                       ")
        builder.append("\n&eCurrency Commands")
        builder.append("\n&f  &c/currency add <name> <abbreviation> <symbol>")
        builder.append("\n&f  &c/currency remove <name>")
        builder.append("\n&f  &c/currency update <name> <setting> <value>")
        builder.append("\n&f  &c/currency info <name>")
        builder.append("\n&f  &c/currency list")
        builder.append("\n&b&m                       ")

        player.sendMessage(UtilString.colour(builder.toString()))
    }

    /**
     * Adds a new currency.
     *
     * @param player        the command sender.
     * @param name          the name of the currency.
     * @param abbreviation  the abbreviation of the currency
     * @param symbol        the symbol/prefix to be used by the currency
     */
    @Command("currency")
    @SubCommand("add")
    @Permission(["currency.admin"])
    @Usage("/currency add <name> <abbreviation> <symbol>")
    fun onCurrencyAdd(player: Player, name: String, abbreviation: String, symbol: String) {
        if (cache.existsByName(name)) {
            localeMessenger.sendMessage(player, "currency.found")
            return
        }

        val size = cache.getAll().size
        val global = size <= 0
        val currency = Currency(name = name, abbreviation = abbreviation.uppercase(Locale.getDefault()), symbol = symbol, main = global)
        val inserted = cache.insert(currency.id, currency)

        inserted.ifPresent { returned ->
            framework.runTaskAsync { service.insert(returned) }
            framework.callEvent(CurrencyAddEvent(returned))
            localeMessenger.sendMessage(player, "currency.commands.add", returned.name)
        }
    }

    /**
     * Removes a currency
     *
     * @param player        the command sender.
     * @param name          the name of the currency.
     */
    @Command("currency")
    @SubCommand("remove")
    @Permission(["currency.admin"])
    @Usage("/currency remove <name>")
    fun onCurrencyRemove(player: Player, name: String) {
        if (!cache.existsByName(name)) {
            localeMessenger.sendMessage(player, "currency.not_found")
            return
        }

        val currency = cache.getByName(name)
        currency.ifPresent { returned ->
            val removed = cache.remove(returned.id)
            removed.ifPresent { deleted ->
                framework.runTaskAsync { service.delete(deleted.id) }
                framework.callEvent(CurrencyRemoveEvent(deleted))
                localeMessenger.sendMessage(player, "currency.commands.remove", deleted.name)
            }
        }
    }

    /**
     * Update a currency field.
     *
     * @param player        the command sender.
     * @param name          the name of the currency.
     * @param setting       the field to be updated.
     * @param value         the value to be used.
     */
    @Command("currency")
    @SubCommand("update")
    @Permission(["currency.admin"])
    @Usage("/currency update <name> <setting> <value>")
    fun onCurrencyUpdate(player: Player, name: String, setting: String, value: String) {
        if (!cache.existsByName(name)) {
            localeMessenger.sendMessage(player, "currency.not_found")
            return
        }

        val currency = cache.getByName(name)
        currency.ifPresent { returned ->
            val updated: Currency = when(setting) {
                "name"  -> returned.updateName(value)
                "abbreviation" -> returned.updateAbbreviation(value)
                "symbol" -> returned.updateSymbol(value)

                else -> {
                    player.sendMessage(UtilString.colour("&4Error: &cValid settings listed: \n  name \n  abbreviation \n  symbol"))
                    return@ifPresent
                }
            }

            cache.update(updated.id, updated)
            framework.runTaskAsync { service.update(updated) }
            localeMessenger.sendMessage(player, "currency.commands.update", setting, value)
        }
    }

    /**
     * Show information on the currency
     *
     * @param player        the command sender.
     * @param name          the name of the currency.
     */
    @Command("currency")
    @SubCommand("info")
    @Permission(["currency.admin"])
    @Usage("/currency info <name>")
    fun onCurrencyInfo(player: Player, name: String) {
        if (!cache.existsByName(name)) {
            localeMessenger.sendMessage(player, "currency.not_found")
            return
        }

        val currency = cache.getByName(name)
        currency.ifPresent { returned -> player.sendMessage(UtilString.colour(returned.info())) }
    }

    /**
     * List all the currencies saved.
     *
     * @param player        the command sender.
     */
    @Command("currency")
    @SubCommand("list")
    @Permission(["currency.admin"])
    @Usage("/currency list")
    fun onCurrencyList(player: Player) {
        val builder = StringBuilder()
        builder.append("&b&m                        ")
        cache.getAll().forEach { builder.append("\n${it.info()}") }
        builder.append("\n&b&m                        ")
        player.sendMessage(UtilString.colour(builder.toString()))
    }
}