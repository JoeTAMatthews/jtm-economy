package com.jtmnetwork.economy.core.usecase.resolver

import com.google.inject.Inject
import com.jtm.framework.core.usecase.command.resolver.ContextResolver
import com.jtm.framework.core.util.UtilString
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.data.cache.CurrencyCache
import org.bukkit.command.CommandSender

class CurrencyResolver(private val cache: CurrencyCache): ContextResolver<Currency> {

    override fun resolve(input: String): Currency? {
        return cache.getByName(input)
    }

    override fun sendError(sender: CommandSender, input: String) {
        sender.sendMessage(UtilString.colour("&4Error: &cCurrency '&f${input}&c' not found."))
    }
}