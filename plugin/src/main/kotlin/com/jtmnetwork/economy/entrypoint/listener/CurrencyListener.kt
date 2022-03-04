package com.jtmnetwork.economy.entrypoint.listener

import com.google.inject.Inject
import com.jtmnetwork.economy.core.domain.event.currency.CurrencyAddEvent
import com.jtmnetwork.economy.core.domain.event.currency.CurrencyRemoveEvent
import com.jtmnetwork.economy.data.cache.WalletCache
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.slf4j.LoggerFactory
import java.util.*

class CurrencyListener @Inject constructor(private val cache: WalletCache): Listener {

    private val logger = LoggerFactory.getLogger(CurrencyListener::class.java)

    /**
     * Handle currency add event by adding a new currency balance to all the wallets.
     *
     * @param event     the currency add event.
     */
    @EventHandler
    fun onCurrencyAdd(event: CurrencyAddEvent) {
        val currency = event.currency
        cache.getAll()?.forEach {
            if (!it.hasBalance(currency.id)) {
                val curr = it.addBalance(currency.id)
                cache.update(it.id, curr)
                logger.info("Updated wallet for: ${it.id}")
            }
        }
    }

    /**
     * Handle currency remove event by removing all currency balances from all the wallets.
     *
     * @param event     the currency remove event.
     */
    @EventHandler
    fun onCurrencyRemove(event: CurrencyRemoveEvent) {
        val currency = event.currency
        cache.getAll()?.forEach {
            if (it.hasBalance(currency.id)) {
                val curr = it.removeBalance(currency.id)
                cache.update(it.id, curr)
                logger.info("Updated wallet for: ${it.id}")
            }
        }
    }
}