package com.jtmnetwork.economy.entrypoint.ui

import com.jtm.framework.core.domain.model.ui.UIInventory
import com.jtm.framework.core.domain.model.ui.UISettings
import com.jtm.framework.core.util.ItemStackBuilder
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.cache.WalletCache
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class WalletUI(private val wallet: Wallet, private val currencyCache: CurrencyCache, private val player: Player): UIInventory(UISettings("wallet", "&bWallet", 9, false, 1)) {

    /**
     * This will set up all balances for the wallet given.
     */
    override fun initItems() {
        val builder = ItemStackBuilder(Material.SUNFLOWER)
            .withName("&b${player.name} Information")
            .withLore(arrayOf(""))

        wallet.balances.forEach {
            val currency = currencyCache.getById(it.key) ?: return
            builder.withLore(arrayOf("&f${currency.name}: &e${currency.getSymbolAmount(it.value)}"))
        }

        registerItem(4, builder.build())
    }

    override fun onInventoryClick(event: InventoryClickEvent, item: ItemStack) {}
}