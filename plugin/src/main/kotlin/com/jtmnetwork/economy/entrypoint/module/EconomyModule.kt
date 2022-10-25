package com.jtmnetwork.economy.entrypoint.module

import com.google.inject.AbstractModule
import com.jtmnetwork.economy.entrypoint.commands.EconomyCommands
import com.jtmnetwork.economy.entrypoint.commands.ExchangeCommands

class EconomyModule: AbstractModule() {
    override fun configure() {
        bind(EconomyCommands::class.java)
        bind(ExchangeCommands::class.java)
    }
}