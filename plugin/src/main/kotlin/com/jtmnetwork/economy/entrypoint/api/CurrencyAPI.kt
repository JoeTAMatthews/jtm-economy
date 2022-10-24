package com.jtmnetwork.economy.entrypoint.api

import com.jtmnetwork.economy.core.domain.entity.Currency
import org.bukkit.command.CommandSender
import java.util.*

interface CurrencyAPI {

    /**
     * Return currency that has been set as main to be used.
     *
     * @param sender        the command sender.
     *
     * @return              the primary currency found.
     */
    fun getPrimaryCurrency(sender: CommandSender?): Optional<Currency>

    /**
     * Return the currency found by the identifier.
     *
     * @param sender        the command sender.
     * @param id            the identifier.
     *
     * @return              the currency found.
     */
    fun getCurrency(sender: CommandSender?, id: UUID): Optional<Currency>

    /**
     * Return the currency found by the name.
     *
     * @param sender        the command sender.
     * @param name          the name.
     *
     * @return              the currency found.
     */
    fun getCurrency(sender: CommandSender?, name: String): Optional<Currency>

    /**
     * Return the currencies persisted.
     *
     * @return              the list of currencies.
     */
    fun getCurrencies(): List<Currency>
}