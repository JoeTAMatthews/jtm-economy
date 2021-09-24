package com.jtmnetwork.economy.entrypoint.module

import com.google.inject.AbstractModule
import com.jtmnetwork.economy.data.cache.ExchangeRateCache
import com.jtmnetwork.economy.data.service.ExchangeRateService
import com.jtmnetwork.economy.entrypoint.commands.ExchangeRateCommands

class ExchangeRateModule: AbstractModule() {

    override fun configure() {
        bind(ExchangeRateCache::class.java)
        bind(ExchangeRateService::class.java)
        bind(ExchangeRateCommands::class.java)
    }
}