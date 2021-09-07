package com.jtmnetwork.economy.data.worker

import com.jtm.framework.Framework
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.core.domain.event.wallet.WalletLoadEvent
import com.jtmnetwork.economy.data.service.WalletService
import org.bukkit.entity.Player

class WalletLoader(private val framework: Framework, private val service: WalletService, private val player: Player): Runnable {

    override fun run() {
        val wallet: Wallet = if (!service.exists(player.uniqueId.toString())) service.insert(Wallet(player.uniqueId, player.name)) ?: return else service.get(player.uniqueId.toString()) ?: return
        framework.runTask { framework.callEvent(WalletLoadEvent(wallet)) }
    }
}