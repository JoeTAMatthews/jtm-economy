package com.jtmnetwork.economy.entrypoint.commands

import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.TransactionService
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class WalletCommandsTest {

    private val transactionService: TransactionService = mock()
    private val walletCache: WalletCache = mock()
    private val currencyCache: CurrencyCache = mock()
    private val walletCommands = WalletCommands(transactionService, walletCache, currencyCache)

    private val player: Player = mock()
    private val target: OfflinePlayer = mock()

}