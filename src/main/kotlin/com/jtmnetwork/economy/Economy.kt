package com.jtmnetwork.economy

import com.google.inject.Injector
import com.jtm.framework.Framework
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.ExchangeRate
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.cache.ExchangeRateCache
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import com.jtmnetwork.economy.entrypoint.commands.*
import com.jtmnetwork.economy.entrypoint.listener.CurrencyListener
import com.jtmnetwork.economy.entrypoint.listener.PlayerListener
import com.jtmnetwork.economy.entrypoint.listener.WalletListener
import com.jtmnetwork.economy.entrypoint.module.CurrencyModule
import com.jtmnetwork.economy.entrypoint.module.EconomyModule
import com.jtmnetwork.economy.entrypoint.module.ExchangeRateModule
import com.jtmnetwork.economy.entrypoint.module.WalletModule

class Economy: Framework(false) {

    private lateinit var subInjector: Injector

    override fun setup() {
        subInjector = injector(listOf(WalletModule(), CurrencyModule(), EconomyModule(), ExchangeRateModule()))

        registerClass(Wallet::class.java)
        registerClass(Currency::class.java)
        registerClass(Transaction::class.java)
        registerClass(ExchangeRate::class.java)
    }

    override fun init() {
        getExchangeRateCache().init()
        getCurrencyCache().init()
    }

    override fun enable() {
        getExchangeRateCache().enable()
        getCurrencyCache().enable()
        getWalletCache().enable()
    }

    override fun disable() {
        getExchangeRateCache().disable()
        getCurrencyCache().disable()
        getWalletCache().disable()
    }

    override fun registerCommands() {
        super.registerCommands()
        registerCommands(subInjector.getInstance(CurrencyCommands::class.java), arrayOf("currency"))
        registerCommands(subInjector.getInstance(WalletCommands::class.java), arrayOf("wallet"))
        registerCommands(subInjector.getInstance(EconomyCommands::class.java), arrayOf("econ"))
        registerCommands(subInjector.getInstance(ExchangeRateCommands::class.java), arrayOf("erate"))
        registerCommands(subInjector.getInstance(ExchangeCommands::class.java), arrayOf("exchange"))
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

    private fun getExchangeRateCache(): ExchangeRateCache {
        return subInjector.getInstance(ExchangeRateCache::class.java)
    }

    fun getEconomyAPI(): EconomyAPI {
        return subInjector.getInstance(EconomyAPI::class.java)
    }
}