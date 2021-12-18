package com.jtmnetwork.economy.data.cache

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.redis.RedisConnector
import com.jtm.framework.core.util.Logging
import com.jtm.framework.data.service.CacheService
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.usecase.resolver.CurrencyResolver
import com.jtmnetwork.economy.data.service.CurrencyService
import java.util.*
import kotlin.math.log

@Singleton
class CurrencyCache @Inject constructor(private val framework: Framework, private val service: CurrencyService, connector: RedisConnector): CacheService<Currency, UUID>(connector,"currency", Currency::class.java) {

    private val logging = framework.getLogging()

    fun init() {
        framework.registerResolver(Currency::class.java, CurrencyResolver(this))
    }

    fun enable() {
        logging.info("Registering currencies:")
        service.getAll()?.forEach {
            val curr = insert(it.id, it) ?: return
            logging.info("- ${curr.name}")
        }
    }

    fun disable() {
        logging.info("Saving currencies:")
        getAll()?.forEach {
            val curr = service.update(it) ?: return
            logging.info("- ${curr.name}")
        }
    }

    fun existsByName(name: String): Boolean {
        getAll()?.forEach { if (it.name.equals(name, true)) return true }
        return false
    }

    fun getByName(name: String): Currency? {
        getAll()?.forEach { if (it.name.equals(name, true)) return it }
        return null
    }
}