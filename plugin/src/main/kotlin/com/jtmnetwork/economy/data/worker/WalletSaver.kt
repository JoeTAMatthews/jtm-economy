package com.jtmnetwork.economy.data.worker

import com.jtm.framework.Framework
import com.jtmnetwork.economy.core.domain.event.wallet.WalletUnloadEvent
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.WalletService
import org.bukkit.entity.Player

class WalletSaver(private val framework: Framework, private val service: WalletService, private val cache: WalletCache, private val player: Player): Runnable {

    override fun run() {
        val wallet = cache.getById(player.uniqueId.toString()) ?: return
        val update = service.update(wallet) ?: return
        framework.runTask { framework.callEvent(WalletUnloadEvent(update)) }
    }
}