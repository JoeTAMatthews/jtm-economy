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
import com.jtmnetwork.economy.entrypoint.vault.VaultEconomy
import org.bukkit.plugin.ServicePriority

class Economy: Framework(false) {

    companion object {
        lateinit var instance: Economy
        private set
    }

    private lateinit var subInjector: Injector

    override fun setup() {
        instance = this
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

        registerVault()
    }

    override fun disable() {
        getExchangeRateCache().disable()
        getCurrencyCache().disable()
        getWalletCache().disable()
    }

    override fun registerCommands() {
        super.registerCommands()
        registerCommands(subInjector.getInstance(CurrencyCommands::class.java), arrayOf("currency"))
        registerCommands(subInjector.getInstance(WalletCommands::class.java), arrayOf("wallet", "transactions"))
        registerCommands(subInjector.getInstance(EconomyCommands::class.java), arrayOf("econ"))
        registerCommands(subInjector.getInstance(ExchangeRateCommands::class.java), arrayOf("erate"))
        registerCommands(subInjector.getInstance(ExchangeCommands::class.java), arrayOf("exchange"))
        registerCommands(subInjector.getInstance(PayCommands::class.java), arrayOf("pay"))
        registerCommands(subInjector.getInstance(RollbackCommands::class.java), arrayOf("rollback"))
    }

    override fun registerListeners() {
        super.registerListeners()
        registerListener(subInjector.getInstance(PlayerListener::class.java))
        registerListener(subInjector.getInstance(WalletListener::class.java))
        registerListener(subInjector.getInstance(CurrencyListener::class.java))
    }

    fun registerVault() {
        if (!server.pluginManager.isPluginEnabled("Vault")) {
            getLogging().warn("Vault not found, using standard EconomyAPI!")
            return
        }
        val vault = server.pluginManager.getPlugin("Vault") ?: return
        server.servicesManager.register(net.milkbowl.vault.economy.Economy::class.java, VaultEconomy(), vault, ServicePriority.High)
        getLogging().info("Vault found. Registered Vault Economy Service provider.")
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