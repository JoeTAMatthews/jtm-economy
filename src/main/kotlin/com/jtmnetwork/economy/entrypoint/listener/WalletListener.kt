package com.jtmnetwork.economy.entrypoint.listener

import com.google.inject.Inject
import com.jtmnetwork.economy.core.domain.event.wallet.WalletLoadEvent
import com.jtmnetwork.economy.core.domain.event.wallet.WalletUnloadEvent
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.cache.WalletCache
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.slf4j.LoggerFactory

class WalletListener @Inject constructor(private val cache: WalletCache, private val currencyCache: CurrencyCache): Listener {

    private val logger = LoggerFactory.getLogger(WalletListener::class.java)

    @EventHandler
    fun onLoad(event: WalletLoadEvent) {
        val wallet = event.wallet
        val saved = cache.insert(wallet.id, wallet) ?: return
        currencyCache.getAll()?.forEach { if (!wallet.hasBalance(it)) wallet.addBalance(it) }
        wallet.balances.keys.forEach { if (!currencyCache.exists(it)) wallet.removeBalance(it) }
        logger.info("Successfully loaded wallet: ${saved.id}")
    }

    @EventHandler
    fun onUnload(event: WalletUnloadEvent) {
        val wallet = event.wallet
        val removed = cache.remove(wallet.id) ?: return
        logger.info("Successfully unloaded wallet: ${removed.id}")
    }
}