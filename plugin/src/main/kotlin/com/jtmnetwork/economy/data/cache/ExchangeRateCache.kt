package com.jtmnetwork.economy.data.cache

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.redis.RedisConnector
import com.jtm.framework.data.service.CacheService
import com.jtmnetwork.economy.core.domain.constants.ExchangeRateSetting
import com.jtmnetwork.economy.core.domain.entity.ExchangeRate
import com.jtmnetwork.economy.core.usecase.resolver.ExchangeRateSettingResolver
import com.jtmnetwork.economy.data.service.ExchangeRateService
import java.util.*

@Singleton
class ExchangeRateCache @Inject constructor(private val framework: Framework, private val service: ExchangeRateService, connector: RedisConnector): CacheService<ExchangeRate, UUID>(connector, "exchange", ExchangeRate::class.java) {

    private val logging = framework.getLogging()

    fun init() {
        framework.registerResolver(ExchangeRateSetting::class.java, ExchangeRateSettingResolver())
    }

    fun enable() {
        logging.info("Registering exchange rates:")
        service.getAll()?.forEach {
            val rate = insert(it.id, it) ?: return
            logging.info("- ${rate.symbol}")
        }
    }

    fun disable() {
        logging.info("Saving exchange rates:")
        getAll()?.forEach {
            val rate = service.update(it) ?: return
            logging.info("- ${rate.symbol}")
        }
    }

    /**
     * Returns if the exchange rate symbol/pair exists.
     *
     * @param symbol        the pair of the exchange rate.
     * @return              if found return true, if not return false.
     */
    fun existsBySymbol(symbol: String): Boolean {
        return getBySymbol(symbol) != null
    }

    /**
     * Returns that exchange rate using the symbol/pair
     *
     * @param symbol        the pair of the exchange rate
     * @return              the exchange rate found, if not found return null
     */
    fun getBySymbol(symbol: String): ExchangeRate? {
        val list = getAll() ?: return null
        return list.stream().filter { it.symbol == symbol }.findFirst().orElse(null)
    }
}