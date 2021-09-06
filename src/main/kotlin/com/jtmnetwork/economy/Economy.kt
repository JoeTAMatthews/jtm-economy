package com.jtmnetwork.economy

import com.google.inject.Injector
import com.jtm.framework.Framework
import com.jtmnetwork.economy.entrypoint.listener.PlayerListener
import com.jtmnetwork.economy.entrypoint.listener.WalletListener
import com.jtmnetwork.economy.entrypoint.module.WalletModule

class Economy: Framework(false) {

    private lateinit var subInjector: Injector

    override fun init() {
        subInjector = injector(listOf(WalletModule()))
    }

    override fun enable() {}

    override fun disable() {}

    override fun registerCommands() {
        super.registerCommands()
    }

    override fun registerListeners() {
        super.registerListeners()
        registerListener(subInjector.getInstance(PlayerListener::class.java))
        registerListener(subInjector.getInstance(WalletListener::class.java))
    }
}