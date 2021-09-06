package com.jtmnetwork.economy.data.cache

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.core.usecase.redis.RedisConnector
import com.jtm.framework.data.service.CacheService
import com.jtmnetwork.economy.core.domain.entity.Wallet
import java.util.*

@Singleton
class WalletCache @Inject constructor(connector: RedisConnector): CacheService<Wallet, UUID>(connector,"wallet", Wallet::class.java)