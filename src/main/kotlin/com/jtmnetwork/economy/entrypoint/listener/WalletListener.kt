package com.jtmnetwork.economy.entrypoint.listener

import com.google.inject.Inject
import com.jtmnetwork.economy.core.domain.event.WalletLoadEvent
import com.jtmnetwork.economy.core.domain.event.WalletUnloadEvent
import com.jtmnetwork.economy.data.cache.WalletCache
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.slf4j.LoggerFactory

class WalletListener @Inject constructor(private val cache: WalletCache): Listener {

    private val logger = LoggerFactory.getLogger(WalletListener::class.java)

    @EventHandler
    fun onLoad(event: WalletLoadEvent) {
        val wallet = event.wallet
        val saved = cache.insert(wallet.id, wallet) ?: return
        logger.info("Successfully loaded wallet: ${saved.id}")
    }

    @EventHandler
    fun onUnload(event: WalletUnloadEvent) {
        val wallet = event.wallet
        val removed = cache.remove(wallet.id) ?: return
        logger.info("Successfully unloaded wallet: ${removed.id}")
    }
}