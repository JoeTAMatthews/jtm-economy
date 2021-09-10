package com.jtmnetwork.economy.data.cache

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.redis.RedisConnector
import com.jtm.framework.data.service.CacheService
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.usecase.resolver.CurrencyResolver
import com.jtmnetwork.economy.data.service.CurrencyService
import java.util.*

@Singleton
class CurrencyCache @Inject constructor(private val framework: Framework, private val service: CurrencyService, connector: RedisConnector): CacheService<Currency, UUID>(connector,"currency", Currency::class.java) {

    fun init() {
        framework.registerResolver(Currency::class.java, CurrencyResolver(this))
    }

    fun enable() {
        service.getAll()?.forEach { insert(it.id, it) }
    }

    fun disable() {
        getAll()?.forEach { service.update(it) }
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