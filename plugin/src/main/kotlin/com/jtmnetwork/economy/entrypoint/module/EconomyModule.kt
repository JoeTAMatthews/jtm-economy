package com.jtmnetwork.economy.entrypoint.module

import com.google.inject.AbstractModule
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import com.jtmnetwork.economy.entrypoint.api.DefaultEconomyAPI
import com.jtmnetwork.economy.entrypoint.commands.EconomyCommands
import com.jtmnetwork.economy.entrypoint.commands.ExchangeCommands

class EconomyModule: AbstractModule() {
    override fun configure() {
        bind(EconomyAPI::class.java).to(DefaultEconomyAPI::class.java)
        bind(EconomyCommands::class.java)
        bind(ExchangeCommands::class.java)
    }
}