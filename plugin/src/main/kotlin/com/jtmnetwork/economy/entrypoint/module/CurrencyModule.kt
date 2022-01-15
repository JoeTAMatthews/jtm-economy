package com.jtmnetwork.economy.entrypoint.module

import com.google.inject.AbstractModule
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.service.CurrencyService
import com.jtmnetwork.economy.entrypoint.commands.CurrencyCommands
import com.jtmnetwork.economy.entrypoint.listener.CurrencyListener

class CurrencyModule: AbstractModule() {

    override fun configure() {
        bind(CurrencyCache::class.java)
        bind(CurrencyService::class.java)
        bind(CurrencyCommands::class.java)
        bind(CurrencyListener::class.java)
    }
}