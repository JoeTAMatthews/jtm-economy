package com.jtmnetwork.economy.entrypoint.api

import com.jtm.framework.Framework
import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.TransactionService
import com.jtmnetwork.economy.data.service.WalletService
import org.bukkit.OfflinePlayer
import org.bukkit.block.data.type.Wall
import org.bukkit.entity.Player
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class EconomyAPIImplTest {

    private val framework: Framework = mock()
    private val transactionService: TransactionService = mock()
    private val walletService: WalletService = mock()
    private val walletCache: WalletCache = mock()
    private val currencyCache: CurrencyCache = mock()
    private lateinit var economyAPI: EconomyAPI

    private val player: Player = mock()
    private val offlinePlayer: OfflinePlayer = mock()
    private val wallet = Wallet(UUID.randomUUID(), "test")
    private val depositTrans = Transaction(type = TransactionType.IN, playerId = UUID.randomUUID(), currency = UUID.randomUUID(), amount = 2.0, balance = 5.0)
    private val withdrawTrans = Transaction(type = TransactionType.OUT, playerId = UUID.randomUUID(), currency = UUID.randomUUID(), amount = 4.0, balance = 6.0)

    @Before
    fun setup() {
        `when`(walletCache.service).thenReturn(walletService)

        economyAPI = EconomyAPIImpl(framework, transactionService, walletCache, currencyCache)

        `when`(player.uniqueId).thenReturn(UUID.randomUUID())
        `when`(offlinePlayer.uniqueId).thenReturn(UUID.randomUUID())

        wallet.addBalance(UUID.randomUUID())
    }

    @Test
    fun depositPlayer_thenWalletNull() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(null)

        val returned = economyAPI.deposit(player, UUID.randomUUID(), UUID.randomUUID(), 2.0)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verifyNoMoreInteractions(walletCache)
        verifyNoInteractions(framework)

        assertFalse { returned }
    }

    @Test
    fun depositPlayer_thenTransactionNull() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.deposit(anyOrNull(), anyOrNull(), anyDouble())).thenReturn(null)

        val returned = economyAPI.deposit(player, UUID.randomUUID(), UUID.randomUUID(),53.0)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verify(walletCache, times(1)).deposit(anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)
        verifyNoInteractions(framework)

        assertFalse(returned)
    }

    @Test
    fun depositPlayer() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.deposit(anyOrNull(), anyOrNull(), anyDouble())).thenReturn(depositTrans)

        val returned = economyAPI.deposit(player, UUID.randomUUID(), UUID.randomUUID(),2.0)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verify(walletCache, times(1)).deposit(anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        assertTrue { returned }
    }

    @Test
    fun depositOfflinePlayer_thenWalletNull() {
        `when`(walletService.get(anyOrNull())).thenReturn(null)

        val returned = economyAPI.deposit(offlinePlayer, UUID.randomUUID(), UUID.randomUUID(), 2.0)

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        verify(walletCache, times(1)).service
        verifyNoMoreInteractions(walletCache)

        verifyNoInteractions(transactionService)

        assertFalse(returned)
    }

    @Test
    fun depositOfflinePlayer_thenTransactionNull() {
        `when`(walletService.get(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.deposit(anyOrNull(), anyOrNull(), anyDouble())).thenReturn(null)

        val returned = economyAPI.deposit(offlinePlayer, UUID.randomUUID(), UUID.randomUUID(), 3.0)

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).deposit(anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)

        verifyNoInteractions(transactionService)

        assertFalse(returned)
    }

    @Test
    fun depositOfflinePlayer() {
        `when`(walletService.get(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.deposit(anyOrNull(), anyOrNull(), anyDouble())).thenReturn(depositTrans)

        val returned = economyAPI.deposit(offlinePlayer, UUID.randomUUID(), UUID.randomUUID(), 2.0)

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).deposit(anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)

        verify(transactionService, times(1)).insert(anyOrNull())
        verifyNoMoreInteractions(transactionService)

        assertTrue(returned)
    }

    @Test
    fun withdrawPlayer_thenWalletNull() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(null)

        val returned = economyAPI.withdraw(player, UUID.randomUUID(), UUID.randomUUID(),5.0)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verifyNoMoreInteractions(walletCache)

        verifyNoInteractions(framework)

        assertFalse(returned)
    }

    @Test
    fun withdrawPlayer_thenTransactionNull() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.withdraw(anyOrNull(), anyOrNull(), anyDouble())).thenReturn(null)

        val returned = economyAPI.withdraw(player, UUID.randomUUID(), UUID.randomUUID(),5.0)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verify(walletCache, times(1)).withdraw(anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)

        verifyNoInteractions(framework)

        assertFalse(returned)
    }

    @Test
    fun withdrawPlayer() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.withdraw(anyOrNull(), anyOrNull(), anyDouble())).thenReturn(withdrawTrans)

        val returned = economyAPI.withdraw(player, UUID.randomUUID(), UUID.randomUUID(),3.0)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verify(walletCache, times(1)).withdraw(anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        assertTrue(returned)
    }

    @Test
    fun withdrawOfflinePlayer_thenWalletNull() {
        `when`(walletService.get(anyOrNull())).thenReturn(null)

        val returned = economyAPI.withdraw(offlinePlayer, UUID.randomUUID(), UUID.randomUUID(),3.0)

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        verify(walletCache, times(1)).service
        verifyNoMoreInteractions(walletCache)
        verifyNoInteractions(transactionService)

        assertFalse(returned)
    }

    @Test
    fun withdrawOfflinePlayer_thenTransactionNull() {
        `when`(walletService.get(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.withdraw(anyOrNull(), anyOrNull(), anyDouble())).thenReturn(null)

        val returned = economyAPI.withdraw(offlinePlayer, UUID.randomUUID(), UUID.randomUUID(),2.0)

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).withdraw(anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)
        verifyNoInteractions(transactionService)

        assertFalse(returned)
    }

    @Test
    fun withdrawOfflinePlayer() {
        `when`(walletService.get(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.withdraw(anyOrNull(), anyOrNull(), anyDouble())).thenReturn(withdrawTrans)

        val returned = economyAPI.withdraw(offlinePlayer, UUID.randomUUID(), UUID.randomUUID(),1.0)

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).withdraw(anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)

        verify(transactionService, times(1)).insert(anyOrNull())
        verifyNoMoreInteractions(transactionService)

        assertTrue(returned)
    }

    @Test
    fun balancePlayer_thenWalletNull() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(null)

        val returned = economyAPI.balance(player, UUID.randomUUID())

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verifyNoMoreInteractions(walletCache)

        assertNull(returned)
    }

    @Test
    fun balancePlayer() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(wallet)

        val returned = economyAPI.balance(player, UUID.randomUUID())

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verifyNoMoreInteractions(walletCache)

        assertNotNull(returned)
        assertEquals(0.0, returned)
    }

    @Test
    fun balanceOfflinePlayer_thenWalletNull() {
        `when`(walletService.get(anyOrNull())).thenReturn(null)

        val returned = economyAPI.balance(offlinePlayer, UUID.randomUUID())

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        assertNull(returned)
    }

    @Test
    fun balanceOfflinePlayer() {
        `when`(walletService.get(anyOrNull())).thenReturn(wallet)

        val returned = economyAPI.balance(offlinePlayer, UUID.randomUUID())

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        assertNotNull(returned)
        assertEquals(0.0, returned)
    }

    @Test
    fun getTransactionsWithCurrencyPlayer() {
        `when`(transactionService.getByPlayerIdAndCurrency(anyOrNull(), anyOrNull())).thenReturn(listOf(withdrawTrans))

        val returned = economyAPI.getTransactions(player, UUID.randomUUID())

        verify(transactionService, times(1)).getByPlayerIdAndCurrency(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(transactionService)

        assertEquals(1, returned.size)
    }

    @Test
    fun getTransactionsWithCurrencyOfflinePlayer() {
        `when`(transactionService.getByPlayerIdAndCurrency(anyOrNull(), anyOrNull())).thenReturn(listOf(withdrawTrans))

        val returned = economyAPI.getTransactions(offlinePlayer, UUID.randomUUID())

        verify(transactionService, times(1)).getByPlayerIdAndCurrency(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(transactionService)

        assertEquals(1, returned.size)
    }

    @Test
    fun getTransactionsPlayer() {
        `when`(transactionService.getByPlayerId(anyOrNull())).thenReturn(listOf(withdrawTrans))

        val returned = economyAPI.getTransactions(player)

        verify(transactionService, times(1)).getByPlayerId(anyOrNull())
        verifyNoMoreInteractions(transactionService)

        assertEquals(1, returned.size)
    }

    @Test
    fun getTransactionOfflinePlayer() {
        `when`(transactionService.getByPlayerId(anyOrNull())).thenReturn(listOf(withdrawTrans))

        val returned = economyAPI.getTransactions(offlinePlayer)

        verify(transactionService, times(1)).getByPlayerId(anyOrNull())
        verifyNoMoreInteractions(transactionService)

        assertEquals(1, returned.size)
    }
}