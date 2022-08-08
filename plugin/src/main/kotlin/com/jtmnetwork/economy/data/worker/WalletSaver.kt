package com.jtmnetwork.economy.data.worker

import com.jtm.framework.Framework
import com.jtmnetwork.economy.core.domain.event.wallet.WalletUnloadEvent
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.WalletService
import org.bukkit.entity.Player

class WalletSaver(private val framework: Framework, private val service: WalletService, private val cache: WalletCache, private val player: Player): Runnable {

    private val logging = framework.getLogging()

    override fun run() {
        val opt = cache.getById(player.uniqueId.toString())
        opt.ifPresent { wallet ->
            val optUpdate = service.update(wallet)
            optUpdate.ifPresent { update -> framework.runTask { framework.callEvent(WalletUnloadEvent(update)) } }
        }
    }
}