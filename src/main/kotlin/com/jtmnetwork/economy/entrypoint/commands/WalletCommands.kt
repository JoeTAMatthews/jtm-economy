package com.jtmnetwork.economy.entrypoint.commands

import com.google.inject.Inject
import com.jtm.framework.core.domain.annotations.Command
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.entrypoint.ui.WalletUI
import org.bukkit.entity.Player

class WalletCommands @Inject constructor(private val walletCache: WalletCache, private val currencyCache: CurrencyCache) {

    @Command("wallet")
    fun onWallet(player: Player) {
        val walletUI = WalletUI(walletCache, currencyCache, player)
        walletUI.showInventory(player, true)
    }
}