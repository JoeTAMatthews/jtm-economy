package com.jtmnetwork.economy.data.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.database.DatabaseConnector
import com.jtm.framework.data.service.Service
import com.jtmnetwork.economy.core.domain.entity.Currency
import okhttp3.internal.format
import org.bukkit.command.CommandSender
import java.util.*

@Singleton
class CurrencyService @Inject constructor(framework: Framework, connector: DatabaseConnector): Service<Currency, UUID>(framework, connector, Currency::class.java) {

    private val messenger = framework.getLocaleMessenger()
    private val logging = framework.getLogging()

    fun getPrimaryCurrency(sender: CommandSender?): Optional<Currency> {
        val opt = getAll().stream().filter { it.main }.findFirst()
        if (opt.isEmpty) {
            if (sender != null) messenger.sendMessage(sender, "currency.not_found")
            logging.debug(format("No primary currency set."))
            return Optional.empty()
        }

        return opt
    }

    fun getCurrency(sender: CommandSender?, id: UUID): Optional<Currency> {
        val opt = get(id)
        if (opt.isEmpty) {
            if (sender != null) messenger.sendMessage(sender, "currency.not_found")
            logging.debug(format("No currency found."))
            return Optional.empty()
        }

        return opt
    }

    fun getCurrency(sender: CommandSender?, name: String): Optional<Currency> {
        val opt = getAll().stream().filter { it.name == name }.findFirst()
        if (opt.isEmpty) {
            if (sender != null) messenger.sendMessage(sender, "currency.not_found")
            logging.debug(format("No currency found."))
            return Optional.empty()
        }

        return opt
    }

    fun getCurrencies(): List<Currency> {
        return getAll()
    }
}