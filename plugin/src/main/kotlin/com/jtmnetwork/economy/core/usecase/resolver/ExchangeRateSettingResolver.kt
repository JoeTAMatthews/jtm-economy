package com.jtmnetwork.economy.core.usecase.resolver

import com.jtm.framework.core.usecase.command.resolver.ContextResolver
import com.jtm.framework.core.util.UtilString
import com.jtmnetwork.economy.core.domain.constants.ExchangeRateSetting
import org.bukkit.command.CommandSender
import java.lang.Exception
import java.util.Optional

class ExchangeRateSettingResolver: ContextResolver<ExchangeRateSetting> {

    override fun resolve(input: String): Optional<ExchangeRateSetting> {
        val rate = ExchangeRateSetting.values().firstOrNull { it.name.equals(input, true) } ?: return Optional.empty()
        return Optional.of(rate)
    }

    override fun sendError(sender: CommandSender, input: String) {
        sender.sendMessage(UtilString.colour("&4Error: &cExchange rate setting '&f${input}&c' not found."))
    }
}