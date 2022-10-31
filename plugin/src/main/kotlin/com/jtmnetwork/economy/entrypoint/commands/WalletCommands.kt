package com.jtmnetwork.economy.entrypoint.commands

import com.google.inject.Inject
import com.jtm.framework.core.domain.annotations.Command
import com.jtm.framework.core.domain.annotations.Optional
import com.jtm.framework.core.domain.annotations.Usage
import com.jtm.framework.core.util.UtilString
import com.jtmnetwork.economy.core.domain.model.TransactionNode
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.TransactionService
import com.jtmnetwork.economy.entrypoint.ui.WalletUI
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class WalletCommands @Inject constructor(private val transactionService: TransactionService, private val walletCache: WalletCache, private val currencyCache: CurrencyCache) {

    private val walletService = walletCache.service

    /**
     * Show the wallet of the target player, if null show the wallet of the command sender.
     *
     * @param player        the command sender.
     */
    @Command("wallet")
    @Usage("/wallet")
    fun onWallet(player: Player, @Optional target: OfflinePlayer?) {
        val wallet = if (target == null) walletCache.getById(player.uniqueId.toString()) else if (target.isOnline) walletCache.getById(target.uniqueId.toString()) else walletService.get(target.uniqueId.toString())
        wallet.ifPresentOrElse({
                val walletUI = WalletUI(it, currencyCache, player)
                walletUI.showInventory(player, true) },
            { player.sendMessage(UtilString.colour("&4Error: &cWallet not found.")) })
    }

    /**
     * Show transactions of the target player, if null show the transactions of the command sender.
     *
     * @param player        the command sender.
     * @param target        the target player.
     */
    @Command("transactions")
    @Usage("/transactions <target>")
    fun onTransactions(player: Player, @Optional target: OfflinePlayer?) {
        val builder = StringBuilder()
        val nodes: Stack<TransactionNode>
        builder.append("&7&m---------------------------------------")

        if (target != null) {
            nodes = transactionService.transactionsToStack(player, target)
            if (nodes.isEmpty()) {
                player.sendMessage(UtilString.colour("&4Error: &cNo transactions."))
                return
            }

            nodes.forEach { builder.append(it.transaction.toString(it.index, currencyCache)) }
        } else {
            nodes = transactionService.transactionsToStack(player, player)
            if (nodes.isEmpty()) {
                player.sendMessage(UtilString.colour("&4Error: &cNo transactions."))
                return
            }

            nodes.forEach { builder.append(it.transaction.toString(it.index, currencyCache)) }
        }

        builder.append("\n&7&m---------------------------------------")
        player.sendMessage(UtilString.colour(builder.toString()))
    }
}