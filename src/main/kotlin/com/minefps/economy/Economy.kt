package com.minefps.economy

import com.google.inject.Injector
import com.jtm.framework.Framework
import com.minefps.economy.entrypoint.module.WalletModule

class Economy: Framework() {

    private lateinit var subInjector: Injector

    override fun init() {
        subInjector = injector(listOf(WalletModule()))
    }

    override fun enable() {
        TODO("Not yet implemented")
    }

    override fun disable() {
        TODO("Not yet implemented")
    }
}