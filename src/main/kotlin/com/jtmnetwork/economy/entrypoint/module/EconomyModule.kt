package com.jtmnetwork.economy.entrypoint.module

import com.google.inject.AbstractModule
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import com.jtmnetwork.economy.entrypoint.api.EconomyAPIImpl
import com.jtmnetwork.economy.entrypoint.commands.EconomyCommands

class EconomyModule: AbstractModule() {
    override fun configure() {
        bind(EconomyAPI::class.java).to(EconomyAPIImpl::class.java)
        bind(EconomyCommands::class.java)
    }
}