package com.jtmnetwork.economy.entrypoint.listener

import com.google.inject.Inject
import com.jtm.framework.Framework
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.WalletService
import com.jtmnetwork.economy.data.worker.WalletLoader
import com.jtmnetwork.economy.data.worker.WalletSaver
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener @Inject constructor(private val framework: Framework, private val service: WalletService, private val cache: WalletCache): Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        framework.runTaskAsync(WalletLoader(framework, service, player))
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        framework.runTaskAsync(WalletSaver(framework, service, cache, player))
    }
}