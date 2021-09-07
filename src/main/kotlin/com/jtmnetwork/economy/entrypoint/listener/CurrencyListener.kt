package com.jtmnetwork.economy.entrypoint.listener

import com.google.inject.Inject
import com.jtmnetwork.economy.core.domain.event.currency.CurrencyAddEvent
import com.jtmnetwork.economy.core.domain.event.currency.CurrencyRemoveEvent
import com.jtmnetwork.economy.data.cache.WalletCache
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.slf4j.LoggerFactory

class CurrencyListener @Inject constructor(private val cache: WalletCache): Listener {

    private val logger = LoggerFactory.getLogger(CurrencyListener::class.java)

    @EventHandler
    fun onCurrencyAdd(event: CurrencyAddEvent) {
        val currency = event.currency
        cache.getAll()?.forEach {
            if (!it.hasBalance(currency)) {
                val curr = it.addBalance(currency)
                cache.update(it.id, curr)
                logger.info("Updated wallet for: ${it.id}")
            }
        }
    }

    @EventHandler
    fun onCurrencyRemove(event: CurrencyRemoveEvent) {
        val currency = event.currency
        cache.getAll()?.forEach {
            if (it.hasBalance(currency)) {
            val curr = it.removeBalance(currency)
            cache.update(it.id, curr)
            logger.info("Updated wallet for: ${it.id}")
        } }
    }
}