package com.jtmnetwork.economy.data.cache

import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.redis.RedisConnector
import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.service.WalletService
import org.bukkit.entity.Player
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyDouble
import org.mockito.internal.verification.VerificationModeFactory.times
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class WalletCacheTest {

    private val framework: Framework = mock()
    private val walletService: WalletService = mock()
    private val connector: RedisConnector = mock()
    private val walletCache = WalletCache(framework, walletService, connector)

    private val currency = UUID.randomUUID()
    private val wallet: Wallet = mock()
    private val player: Player = mock()
    private val id = UUID.randomUUID()

    @Before
    fun setup() {
        `when`(player.uniqueId).thenReturn(id)
    }

    @Test
    fun hasBalance_thenWalletNull() {
        val returned = walletCache.hasBalance(player, currency, 3.0)

        assertFalse(returned)
    }

    @Test
    fun hasBalance_thenBalanceNull() {
        walletCache.insert(id.toString(), wallet)

        val returned = walletCache.hasBalance(player, currency, 5.0)

        verify(wallet, times(1)).balances
        verifyNoMoreInteractions(wallet)

        assertFalse(returned)
    }

    @Test
    fun hasBalance() {
        walletCache.insert(id.toString(), wallet)
        `when`(wallet.balances).thenReturn(mutableMapOf(currency to 5.0))

        val returned = walletCache.hasBalance(player, currency, 2.0)

        verify(wallet, times(1)).balances
        verifyNoMoreInteractions(wallet)

        assertTrue(returned)
    }

    @Test
    fun hasBalance_thenInsufficientBalance() {
        walletCache.insert(id.toString(), wallet)
        `when`(wallet.balances).thenReturn(mutableMapOf(currency to 0.0))

        val returned = walletCache.hasBalance(player, currency, 2.0)

        verify(wallet, times(1)).balances
        verifyNoMoreInteractions(wallet)

        assertFalse(returned)
    }

    @Test
    fun deposit_thenDepositedNull() {
        `when`(wallet.addBalance(anyOrNull(), anyOrNull())).thenReturn(null)

        val returned = walletCache.deposit(wallet, UUID.randomUUID(), 5.0)

        verify(wallet, times(1)).addBalance(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(wallet)

        assertNull(returned)
    }

    @Test
    fun deposit() {
        `when`(wallet.id).thenReturn(id.toString())
        `when`(wallet.addBalance(anyOrNull(), anyOrNull())).thenReturn(wallet)
        `when`(wallet.getBalance(anyOrNull())).thenReturn(1.0)

        val returned = walletCache.deposit(wallet, UUID.randomUUID(), 2.0)

        verify(wallet, times(1)).id
        verify(wallet, times(1)).addBalance(anyOrNull(), anyDouble())
        verify(wallet, times(1)).getBalance(anyOrNull())
        verifyNoMoreInteractions(wallet)

        verify(framework, times(1)).getLogging()
        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        assertNotNull(returned)
        assertEquals(returned!!.type, TransactionType.IN)
    }

    @Test
    fun withdraw_thenWithdrewNull() {
        `when`(wallet.removeBalance(anyOrNull(), anyOrNull())).thenReturn(null)

        val returned = walletCache.withdraw(wallet, currency, 4.0)

        verify(wallet, times(1)).removeBalance(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(wallet)

        assertNull(returned)
    }

    @Test
    fun withdraw() {
        `when`(wallet.id).thenReturn(id.toString())
        `when`(wallet.removeBalance(anyOrNull(), anyOrNull())).thenReturn(wallet)
        `when`(wallet.getBalance(anyOrNull())).thenReturn(1.0)

        val returned = walletCache.withdraw(wallet, currency, 5.0)

        verify(wallet, times(1)).id
        verify(wallet, times(1)).removeBalance(anyOrNull(), anyOrNull())
        verify(wallet, times(1)).getBalance(anyOrNull())
        verifyNoMoreInteractions(wallet)

        verify(framework, times(1)).getLogging()
        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        assertNotNull(returned)
        assertEquals(returned!!.type, TransactionType.OUT)
    }
}