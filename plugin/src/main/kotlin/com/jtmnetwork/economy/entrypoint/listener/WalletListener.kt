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

    /**
     * Handle the load of the wallet by adding all currencies it doesn't have.
     *
     * @param event         the wallet load event.
     */
    @EventHandler
    fun onLoad(event: WalletLoadEvent) {
        val wallet = event.wallet
        val saved = cache.insert(wallet.id, wallet)
        saved.ifPresent { updated ->
            currencyCache.getAll().forEach { if (!wallet.hasBalance(it.id)) wallet.addBalance(it.id) }
            wallet.balances.keys.forEach { if (!currencyCache.exists(it)) wallet.removeBalance(it) }
            logger.info("Successfully loaded wallet: ${updated.id}")
        }
    }

    /**
     * Handle unloading the wallet from the cache.
     *
     * @param event         the wallet unload event.
     */
    @EventHandler
    fun onUnload(event: WalletUnloadEvent) {
        val wallet = event.wallet
        val removed = cache.remove(wallet.id)
        removed.ifPresent { returned -> logger.info("Successfully unloaded wallet: ${returned.id}") }
    }
}