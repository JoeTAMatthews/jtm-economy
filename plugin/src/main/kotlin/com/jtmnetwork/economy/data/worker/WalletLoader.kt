package com.jtmnetwork.economy.data.worker

import com.jtm.framework.Framework
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.core.domain.event.wallet.WalletLoadEvent
import com.jtmnetwork.economy.data.service.WalletService
import org.bukkit.entity.Player

class WalletLoader(private val framework: Framework, private val service: WalletService, private val player: Player): Runnable {

    private val logging = framework.getLogging()

    override fun run() {
        val opt = if (service.exists(player.uniqueId.toString())) service.get(player.uniqueId.toString()) else service.insert(Wallet(player.uniqueId.toString(), player.name))
        opt.ifPresent { wallet -> framework.runTask { framework.callEvent(WalletLoadEvent(wallet)) } }
    }
}