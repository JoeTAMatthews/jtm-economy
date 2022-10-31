package com.jtmnetwork.economy.entrypoint.api.impl

import com.google.inject.Inject
import com.jtm.framework.core.util.Logging
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.data.cache.ExchangeRateCache
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.entrypoint.api.ExchangeAPI
import okhttp3.internal.format
import org.bukkit.entity.Player

class ExchangeAPIImpl @Inject constructor(private val logging: Logging, private val cache: ExchangeRateCache, private val walletCache: WalletCache): ExchangeAPI {

    override fun exchange(player: Player, from: Currency, to: Currency, amount: Double): Boolean {
        val wallet = walletCache.getWallet(player, player)
        if (wallet.isEmpty) return false

        val optExchanged = cache.exchange(player, wallet.get(), from, to, amount)
        if (optExchanged.isEmpty) return false

        val exchanged = optExchanged.get()
        val updated = walletCache.update(exchanged.id, exchanged)
        updated.ifPresent { w -> logging.info(format("%s(%s) has a successful currency exchange.", w.id, w.name)) }
        return true
    }
}