package com.jtmnetwork.economy.entrypoint.module

import com.google.inject.AbstractModule
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.WalletService
import com.jtmnetwork.economy.entrypoint.commands.WalletCommands
import com.jtmnetwork.economy.entrypoint.listener.PlayerListener
import com.jtmnetwork.economy.entrypoint.listener.WalletListener

class WalletModule: AbstractModule() {
    override fun configure() {
        bind(WalletCache::class.java)
        bind(WalletService::class.java)

        bind(PlayerListener::class.java)
        bind(WalletListener::class.java)
        bind(WalletCommands::class.java)
    }
}