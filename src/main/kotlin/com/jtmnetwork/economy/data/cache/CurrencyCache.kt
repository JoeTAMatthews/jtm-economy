package com.jtmnetwork.economy.data.cache

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.core.usecase.redis.RedisConnector
import com.jtm.framework.data.service.CacheService
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.data.service.CurrencyService
import java.util.*

@Singleton
class CurrencyCache @Inject constructor(private val service: CurrencyService, connector: RedisConnector): CacheService<Currency, UUID>(connector,"currency", Currency::class.java) {

    fun enable() {
        service.getAll()?.forEach { insert(it.id, it) }
    }

    fun disable() {
        getAll()?.forEach { service.update(it) }
    }

    fun existsByName(name: String): Boolean {
        getAll()?.forEach { if (it.name.equals(name, false)) return true }
        return false
    }

    fun getByName(name: String): Currency? {
        getAll()?.forEach { if (it.name.equals(name, false)) return it }
        return null
    }
}