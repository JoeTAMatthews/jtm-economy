package com.jtmnetwork.economy

import com.google.inject.Injector
import com.jtm.framework.Framework
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import com.jtmnetwork.economy.entrypoint.commands.CurrencyCommands
import com.jtmnetwork.economy.entrypoint.commands.EconomyCommands
import com.jtmnetwork.economy.entrypoint.commands.WalletCommands
import com.jtmnetwork.economy.entrypoint.listener.CurrencyListener
import com.jtmnetwork.economy.entrypoint.listener.PlayerListener
import com.jtmnetwork.economy.entrypoint.listener.WalletListener
import com.jtmnetwork.economy.entrypoint.module.CurrencyModule
import com.jtmnetwork.economy.entrypoint.module.EconomyModule
import com.jtmnetwork.economy.entrypoint.module.WalletModule

class Economy: Framework(false) {

    private lateinit var subInjector: Injector

    override fun init() {
        subInjector = injector(listOf(WalletModule(), CurrencyModule(), EconomyModule()))

        registerClass(Wallet::class.java)
        registerClass(Currency::class.java)
        registerClass(Transaction::class.java)
    }

    override fun enable() {
        getCurrencyCache().enable()
        getWalletCache().enable()
    }

    override fun disable() {
        getCurrencyCache().disable()
        getWalletCache().disable()
    }

    override fun registerCommands() {
        super.registerCommands()
        registerCommands(subInjector.getInstance(CurrencyCommands::class.java), arrayOf("currency"))
        registerCommands(subInjector.getInstance(WalletCommands::class.java), arrayOf("wallet"))
        registerCommands(subInjector.getInstance(EconomyCommands::class.java), arrayOf("econ"))
    }

    override fun registerListeners() {
        super.registerListeners()
        registerListener(subInjector.getInstance(PlayerListener::class.java))
        registerListener(subInjector.getInstance(WalletListener::class.java))
        registerListener(subInjector.getInstance(CurrencyListener::class.java))
    }

    private fun getWalletCache(): WalletCache {
        return subInjector.getInstance(WalletCache::class.java)
    }

    private fun getCurrencyCache(): CurrencyCache {
        return subInjector.getInstance(CurrencyCache::class.java)
    }

    fun getEconomyAPI(): EconomyAPI {
        return subInjector.getInstance(EconomyAPI::class.java)
    }
}