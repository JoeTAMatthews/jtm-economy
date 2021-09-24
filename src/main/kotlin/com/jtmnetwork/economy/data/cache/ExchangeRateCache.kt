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

    fun init() {
        framework.registerResolver(ExchangeRateSetting::class.java, ExchangeRateSettingResolver())
    }

    fun enable() {
        service.getAll()?.forEach { insert(it.id, it) }
    }

    fun disable() {
        getAll()?.forEach { service.update(it) }
    }

    fun existsBySymbol(symbol: String): Boolean {
        return getBySymbol(symbol) != null
    }

    fun getBySymbol(symbol: String): ExchangeRate? {
        val list = getAll() ?: return null
        return list.stream().filter { it.symbol == symbol }.findFirst().orElse(null)
    }
}