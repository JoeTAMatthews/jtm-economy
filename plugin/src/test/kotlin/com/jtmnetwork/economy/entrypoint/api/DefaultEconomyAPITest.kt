package com.jtmnetwork.economy.entrypoint.api

import com.jtm.framework.Framework
import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.ExchangeRate
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.cache.ExchangeRateCache
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.TransactionService
import com.jtmnetwork.economy.data.service.WalletService
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class DefaultEconomyAPITest {

    private val framework: Framework = mock()
    private val transactionService: TransactionService = mock()
    private val walletService: WalletService = mock()
    private val walletCache: WalletCache = mock()
    private val currencyCache: CurrencyCache = mock()
    private val exchangeRateCache: ExchangeRateCache = mock()
    private lateinit var economyAPI: EconomyAPI

    private val player: Player = mock()
    private val offlinePlayer: OfflinePlayer = mock()
    private val wallet = Wallet(UUID.randomUUID().toString(), "test")
    private val depositTrans = Transaction(type = TransactionType.IN, sender = UUID.randomUUID(), receiver = UUID.randomUUID(), currency = UUID.randomUUID(), amount = 2.0, previous_balance = 3.0, new_balance = 5.0)
    private val withdrawTrans = Transaction(type = TransactionType.OUT, sender = UUID.randomUUID(), receiver = UUID.randomUUID(), currency = UUID.randomUUID(), amount = 4.0, previous_balance = 2.0, new_balance = 6.0)
    private val currency = Currency(name = "test", abbreviation = "tes", symbol = "£")
    private val rate = ExchangeRate(currency_from = UUID.randomUUID(), currency_to = UUID.randomUUID(), symbol = "TestDS", rate = 2.4)
    private val mockWallet: Wallet = mock()
    private val mockCurrency: Currency = mock()

    @Before
    fun setup() {
        `when`(walletCache.service).thenReturn(walletService)

        economyAPI = DefaultEconomyAPI(framework, transactionService, walletCache, currencyCache, exchangeRateCache)

        `when`(player.uniqueId).thenReturn(UUID.randomUUID())
        `when`(offlinePlayer.uniqueId).thenReturn(UUID.randomUUID())
        `when`(mockCurrency.main).thenReturn(true)

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

        assertNotNull(returned)
    }

    @Test
    fun depositPlayer_thenTransactionNull() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.deposit(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())).thenReturn(null)

        val returned = economyAPI.deposit(player, UUID.randomUUID(), UUID.randomUUID(),53.0)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verify(walletCache, times(1)).deposit(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)
        verifyNoInteractions(framework)

        assertNotNull(returned)
    }

    @Test
    fun depositPlayer() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.deposit(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())).thenReturn(depositTrans)

        val returned = economyAPI.deposit(player, UUID.randomUUID(), UUID.randomUUID(),2.0)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verify(walletCache, times(1)).deposit(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        assertNotNull(returned)
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

        assertNotNull(returned)
    }

    @Test
    fun depositOfflinePlayer_thenTransactionNull() {
        `when`(walletService.get(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.deposit(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())).thenReturn(null)

        val returned = economyAPI.deposit(offlinePlayer, UUID.randomUUID(), UUID.randomUUID(), 3.0)

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).deposit(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)

        verifyNoInteractions(transactionService)

        assertNotNull(returned)
    }

    @Test
    fun depositOfflinePlayer() {
        `when`(walletService.get(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.deposit(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())).thenReturn(depositTrans)

        val returned = economyAPI.deposit(offlinePlayer, UUID.randomUUID(), UUID.randomUUID(), 2.0)

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).deposit(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)

        verify(transactionService, times(1)).insert(anyOrNull())
        verifyNoMoreInteractions(transactionService)

        assertNotNull(returned)
    }

    @Test
    fun withdrawPlayer_thenWalletNull() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(null)

        val returned = economyAPI.withdraw(player, UUID.randomUUID(), UUID.randomUUID(),5.0)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verifyNoMoreInteractions(walletCache)

        verifyNoInteractions(framework)

        assertNotNull(returned)
    }

    @Test
    fun withdrawPlayer_thenTransactionNull() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.withdraw(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())).thenReturn(null)

        val returned = economyAPI.withdraw(player, UUID.randomUUID(), UUID.randomUUID(),5.0)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verify(walletCache, times(1)).withdraw(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)

        verifyNoInteractions(framework)

        assertNotNull(returned)
    }

    @Test
    fun withdrawPlayer() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.withdraw(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())).thenReturn(withdrawTrans)

        val returned = economyAPI.withdraw(player, UUID.randomUUID(), UUID.randomUUID(),3.0)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verify(walletCache, times(1)).withdraw(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        assertNotNull(returned)
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

        assertNotNull(returned)
    }

    @Test
    fun withdrawOfflinePlayer_thenTransactionNull() {
        `when`(walletService.get(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.withdraw(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())).thenReturn(null)

        val returned = economyAPI.withdraw(offlinePlayer, UUID.randomUUID(), UUID.randomUUID(),2.0)

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).withdraw(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)
        verifyNoInteractions(transactionService)

        assertNotNull(returned)
    }

    @Test
    fun withdrawOfflinePlayer() {
        `when`(walletService.get(anyOrNull())).thenReturn(wallet)
        `when`(walletCache.withdraw(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())).thenReturn(withdrawTrans)

        val returned = economyAPI.withdraw(offlinePlayer, UUID.randomUUID(), UUID.randomUUID(),1.0)

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).withdraw(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(walletCache)

        verify(transactionService, times(1)).insert(anyOrNull())
        verifyNoMoreInteractions(transactionService)

        assertNotNull(returned)
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
        `when`(transactionService.getByCurrency(anyOrNull(), anyOrNull())).thenReturn(listOf(withdrawTrans))

        val returned = economyAPI.getTransactions(player, UUID.randomUUID())

        verify(transactionService, times(1)).getByCurrency(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(transactionService)

        assertEquals(1, returned.size)
    }

    @Test
    fun getTransactionsWithCurrencyOfflinePlayer() {
        `when`(transactionService.getByCurrency(anyOrNull(), anyOrNull())).thenReturn(listOf(withdrawTrans))

        val returned = economyAPI.getTransactions(offlinePlayer, UUID.randomUUID())

        verify(transactionService, times(1)).getByCurrency(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(transactionService)

        assertEquals(1, returned.size)
    }

    @Test
    fun getTransactionsPlayer() {
        `when`(transactionService.getByReceiver(anyOrNull())).thenReturn(listOf(withdrawTrans))

        val returned = economyAPI.getTransactions(player)

        verify(transactionService, times(1)).getByReceiver(anyOrNull())
        verifyNoMoreInteractions(transactionService)

        assertEquals(1, returned.size)
    }

    @Test
    fun getTransactionOfflinePlayer() {
        `when`(transactionService.getByReceiver(anyOrNull())).thenReturn(listOf(withdrawTrans))

        val returned = economyAPI.getTransactions(offlinePlayer)

        verify(transactionService, times(1)).getByReceiver(anyOrNull())
        verifyNoMoreInteractions(transactionService)

        assertEquals(1, returned.size)
    }

    @Test
    fun exchangeAmountPlayer() {
        `when`(mockWallet.addBalance(anyOrNull(), anyDouble())).thenReturn(mockWallet)
        `when`(mockWallet.removeBalance(anyOrNull(), anyDouble())).thenReturn(mockWallet)
        `when`(currencyCache.getById(anyOrNull())).thenReturn(currency)
        `when`(exchangeRateCache.getBySymbol(anyString())).thenReturn(rate)
        `when`(walletCache.getById(anyOrNull())).thenReturn(mockWallet)
        `when`(walletCache.update(anyOrNull(), anyOrNull())).thenReturn(mockWallet)
        `when`(walletCache.hasBalance(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(true)

        val returned = economyAPI.exchangeAmount(player, UUID.randomUUID(), UUID.randomUUID(), 2.4)

        verify(currencyCache, times(2)).getById(anyOrNull())
        verifyNoMoreInteractions(currencyCache)

        verify(exchangeRateCache, times(1)).getBySymbol(anyString())
        verifyNoMoreInteractions(exchangeRateCache)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).getById(anyOrNull())
        verify(walletCache, times(1)).hasBalance(anyOrNull(), anyOrNull(), anyOrNull())
        verify(walletCache, times(1)).hasBalance(anyOrNull(), anyOrNull(), anyOrNull())
        verify(walletCache, times(1)).update(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(walletCache)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        verify(player, times(1)).uniqueId
        verifyNoMoreInteractions(player)

        assertTrue(returned)
    }

    @Test
    fun exchangeAmountOfflinePlayer() {
        `when`(mockWallet.addBalance(anyOrNull(), anyDouble())).thenReturn(mockWallet)
        `when`(mockWallet.removeBalance(anyOrNull(), anyDouble())).thenReturn(mockWallet)
        `when`(walletCache.hasBalanceOffline(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(true)
        `when`(currencyCache.getById(anyOrNull())).thenReturn(currency)
        `when`(exchangeRateCache.getBySymbol(anyString())).thenReturn(rate)
        `when`(walletService.get(anyOrNull())).thenReturn(mockWallet)

        val returned = economyAPI.exchangeAmountOffline(offlinePlayer, UUID.randomUUID(), UUID.randomUUID(), 2.5)

        verify(currencyCache, times(2)).getById(anyOrNull())
        verifyNoMoreInteractions(currencyCache)

        verify(exchangeRateCache, times(1)).getBySymbol(anyString())
        verifyNoMoreInteractions(exchangeRateCache)

        verify(walletCache, times(1)).service
        verify(walletCache, times(1)).hasBalanceOffline(anyOrNull(), anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(walletCache)

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        verify(offlinePlayer, times(1)).uniqueId
        verifyNoMoreInteractions(offlinePlayer)

        assertTrue(returned)
    }

    @Test
    fun getWalletPlayer() {
        `when`(walletCache.getById(anyOrNull())).thenReturn(mockWallet)

        val returned = economyAPI.getWallet(player)

        verify(walletCache, times(1)).getById(anyOrNull())
        verify(walletCache, times(1)).service
        verifyNoMoreInteractions(walletCache)

        assertNotNull(returned)
    }

    @Test
    fun getWalletOfflinePlayer() {
        `when`(walletService.get(anyOrNull())).thenReturn(mockWallet)

        val returned = economyAPI.getWallet(offlinePlayer)

        verify(walletService, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(walletService)

        assertNotNull(returned)
    }

    @Test
    fun getCurrency() {
        `when`(currencyCache.getById(anyOrNull())).thenReturn(mockCurrency)

        val returned = economyAPI.getCurrency(UUID.randomUUID())

        verify(currencyCache, times(1)).getById(anyOrNull())
        verifyNoMoreInteractions(currencyCache)

        assertNotNull(returned)
    }

    @Test
    fun getCurrencyByName() {
        `when`(currencyCache.getByName(anyString())).thenReturn(mockCurrency)

        val returned = economyAPI.getCurrency("test")

        verify(currencyCache, times(1)).getByName(anyString())
        verifyNoMoreInteractions(currencyCache)

        assertNotNull(returned)
    }

    @Test
    fun getGlobalCurrency() {
        `when`(currencyCache.getGlobalCurrency()).thenReturn(mockCurrency)

        val returned = economyAPI.getGlobalCurrency()

        verify(currencyCache, times(1)).getGlobalCurrency()
        verifyNoMoreInteractions(currencyCache)

        assertNotNull(returned)

        if (returned != null) {
            assertTrue(returned.main)
        }
    }
}