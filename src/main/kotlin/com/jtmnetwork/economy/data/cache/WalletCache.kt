package com.jtmnetwork.economy.data.cache

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.core.usecase.redis.RedisConnector
import com.jtm.framework.data.service.CacheService
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.service.WalletService
import java.util.*

@Singleton
class WalletCache @Inject constructor(private val service: WalletService, connector: RedisConnector): CacheService<Wallet, UUID>(connector,"wallet", Wallet::class.java) {

    fun enable() {
        service.getAll()?.forEach { insert(it.id, it) }
    }

    fun disable() {
        getAll()?.forEach {
            service.update(it)
            remove(it.id)
        }
    }
}