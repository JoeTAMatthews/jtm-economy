package com.jtmnetwork.economy.entrypoint.api.impl

import com.google.inject.Inject
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.data.service.CurrencyService
import com.jtmnetwork.economy.entrypoint.api.CurrencyAPI
import org.bukkit.command.CommandSender
import java.util.*

class CurrencyAPIImpl @Inject constructor(private val currencyService: CurrencyService): CurrencyAPI {

    override fun getPrimaryCurrency(sender: CommandSender?): Optional<Currency> {
        return currencyService.getPrimaryCurrency(sender)
    }

    override fun getCurrency(sender: CommandSender?, id: UUID): Optional<Currency> {
        return currencyService.getCurrency(sender, id)
    }

    override fun getCurrency(sender: CommandSender?, name: String): Optional<Currency> {
        return currencyService.getCurrency(sender, name)
    }

    override fun getCurrencies(): List<Currency> {
        return currencyService.getCurrencies()
    }
}