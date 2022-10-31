package com.jtmnetwork.economy.data.cache

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.redis.RedisConnector
import com.jtm.framework.data.service.CacheService
import com.jtmnetwork.economy.core.domain.constants.ExchangeRateSetting
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.ExchangeRate
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.core.usecase.resolver.ExchangeRateSettingResolver
import com.jtmnetwork.economy.data.service.ExchangeRateService
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@Singleton
class ExchangeRateCache @Inject constructor(private val framework: Framework, private val service: ExchangeRateService, connector: RedisConnector): CacheService<ExchangeRate, UUID>(connector, "exchange", ExchangeRate::class.java) {

    private val logging = framework.getLogging()
    private val messenger = framework.getLocaleMessenger()

    fun init() {
        framework.registerResolver(ExchangeRateSetting::class.java, ExchangeRateSettingResolver())
    }

    fun enable() {
        logging.info("Registering exchange rates:")
        service.getAll().forEach {
            val opt = insert(it.id, it)
            opt.ifPresent { rate -> logging.info("- ${rate.symbol}") }
        }
    }

    fun disable() {
        logging.info("Saving exchange rates:")
        getAll().forEach {
            val opt = service.update(it)
            opt.ifPresent { rate -> logging.info("- ${rate.symbol}") }
        }
    }

    /**
     * Returns if the exchange rate symbol/pair exists.
     *
     * @param symbol        the pair of the exchange rate.
     *
     * @return              if found return true, if not return false.
     */
    fun existsBySymbol(symbol: String): Boolean {
        return getBySymbol(null, symbol).isPresent
    }

    /**
     * Returns that exchange rate using the symbol/pair
     *
     * @param sender        the command sender.
     * @param symbol        the pair of the exchange rate
     *
     * @return              the exchange rate found, if not found return null
     */
    fun getBySymbol(sender: CommandSender?, symbol: String): Optional<ExchangeRate> {
        val currency = getAll().stream().filter { it.symbol.equals(symbol, true) }.findFirst()
        if (currency.isEmpty) {
            if (sender != null) messenger.sendMessage(sender, "rate.not_found")
            logging.warn("No exchange rate found.")
            return Optional.empty()
        }

        return currency
    }

    /**
     * Exchange a set of currencies between each other with a selected amount.
     *
     * @param player        the player initiating the exchange.
     * @param wallet        the wallet to exchange.
     * @param from          the currency being exchanged from.
     * @param to            the currency being exchange to.
     * @param amount        the amount of currency being exchanged.
     *
     * @return              the wallet to be saved after being exchanged.
     */
    fun exchange(player: Player, wallet: Wallet, from: Currency, to: Currency, amount: Double): Optional<Wallet> {
        val symbol = "${from.abbreviation}${to.abbreviation}"
        val optRate = getBySymbol(player, symbol)
        if (optRate.isEmpty) return Optional.empty()
        if (!wallet.checkBalance(from.id, amount)) {
            messenger.sendMessage(player, "user.insufficient_funds")
            return Optional.empty()
        }

        val rate = optRate.get()
        val conversion = amount * (rate.rate)
        val deposited = wallet.addBalance(to.id, conversion)
        if (deposited == null) {
            messenger.sendMessage(player, "rate.failed.deposited")
            return Optional.empty()
        }

        val withdrawn = deposited.removeBalance(from.id, amount)
        if (withdrawn == null) {
            messenger.sendMessage(player, "rate.failed.withdrawn")
            return Optional.empty()
        }

        return Optional.of(withdrawn)
    }
}