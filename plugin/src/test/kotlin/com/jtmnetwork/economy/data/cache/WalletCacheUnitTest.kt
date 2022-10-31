package com.jtmnetwork.economy.data.cache

import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.redis.RedisConnector
import com.jtm.framework.core.util.Logging
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.core.util.TestUtil
import com.jtmnetwork.economy.data.service.TransactionService
import com.jtmnetwork.economy.data.service.WalletService
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.internal.verification.VerificationModeFactory.times
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.util.*
import javax.swing.text.html.Option

@RunWith(MockitoJUnitRunner::class)
class WalletCacheUnitTest {

    private val framework: Framework = mock()
    private val logging: Logging = mock()
    private val messenger: LocaleMessenger = mock()
    private val walletService: WalletService = mock()
    private val transactionService: TransactionService = mock()
    private val connector: RedisConnector = mock()
    private lateinit var cache: WalletCache

    private val player: Player = mock()
    private val id = UUID.randomUUID()
    private val from = UUID.randomUUID()
    private val currency = TestUtil.createCurrency()
    private val wallet = Wallet(id.toString(), "Test")
    private val sender: CommandSender = mock()

    @Before
    fun setup() {
        `when`(player.uniqueId).thenReturn(id)
        `when`(player.name).thenReturn("JTM")

        `when`(framework.getLogging()).thenReturn(logging)
        `when`(framework.getLocaleMessenger()).thenReturn(messenger)

        cache = spy(WalletCache(framework, walletService, transactionService, connector))

        verify(framework, times(1)).getLogging()
        verify(framework, times(1)).getLocaleMessenger()
    }

    @Test
    fun depositSender_thenLogError_whenGettingWallet() {
        `when`(cache.getById(anyString())).thenReturn(Optional.empty())

        val returned = cache.deposit(sender, player, from, currency, 25.0)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun depositSender_thenLogError_whenDepositing() {
        `when`(cache.getById(anyString())).thenReturn(Optional.of(wallet))


        val returned = cache.deposit(sender, player, from, currency, 50.0)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun depositSender_thenReturnTransaction_whenDepositing() {
        `when`(cache.getById(anyString())).thenReturn(Optional.of(wallet.setBalance(currency.id, 150.0)))

        val returned = cache.deposit(sender, player, from, currency, 75.0)

        verify(messenger, times(2)).sendMessage(anyOrNull(), anyString(), anyOrNull())
        verifyNoMoreInteractions(messenger)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        assertTrue(returned.isPresent)

        returned.ifPresent { trans ->
            assertEquals(trans.new_balance, 225.0)
            assertEquals(trans.previous_balance, 150.0)
            assertEquals(trans.type, TransactionType.IN)
            assertEquals(trans.currency, currency.id)
            assertEquals(trans.amount, 75.0)
        }
    }

    @Test
    fun withdrawSender_thenLogError_whenGettingWallet() {
        `when`(cache.getById(anyString())).thenReturn(Optional.empty())

        val returned = cache.withdraw(sender, player, from, currency, 100.0)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun withdrawSender_thenLogError_whenWithdrawing() {
        `when`(cache.getById(anyString())).thenReturn(Optional.of(wallet))

        val returned = cache.withdraw(sender, player, from, currency, 125.0)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun withdrawSender_thenReturnTransaction_whenWithdrawing() {
        `when`(cache.getById(anyString())).thenReturn(Optional.of(wallet.setBalance(currency.id, 250.0)))

        val returned = cache.withdraw(sender, player, from, currency, 150.0)

        verify(messenger, times(2)).sendMessage(anyOrNull(), anyString(), anyOrNull())
        verifyNoMoreInteractions(messenger)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        assertTrue(returned.isPresent)

        returned.ifPresent { trans ->
            assertEquals(trans.previous_balance, 250.0)
            assertEquals(trans.new_balance, 100.0)
            assertEquals(trans.amount, 150.0)
            assertEquals(trans.currency, currency.id)
            assertEquals(trans.type, TransactionType.OUT)
        }
    }

    @Test
    fun balanceSender_thenLogError_whenGettingWallet() {
        `when`(cache.getById(anyString())).thenReturn(Optional.empty())

        val returned = cache.balance(sender, player, currency)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun balanceSender_thenReturnBalance() {
        `when`(cache.getById(anyString())).thenReturn(Optional.of(wallet.setBalance(currency.id, 100.0)))

        val returned = cache.balance(sender, player, currency)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString(), anyOrNull())
        verifyNoMoreInteractions(messenger)

        assertTrue(returned.isPresent)

        returned.ifPresent { bal -> assertEquals(bal, 100.0) }
    }

    @Test
    fun hasBalanceSender_thenLogError_whenGettingWallet() {
        `when`(cache.getById(anyString())).thenReturn(Optional.empty())

        val returned = cache.hasBalance(sender, player, currency, 50.0)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertFalse(returned)
    }

    @Test
    fun hasBalanceSender_thenReturnTrue() {
        `when`(cache.getById(anyString())).thenReturn(Optional.of(wallet.setBalance(currency.id, 150.0)))

        val returned = cache.hasBalance(sender, player, currency, 50.0)

        assertTrue(returned)
    }

    @Test
    fun hasBalance_thenLogError_whenGettingWallet() {
        `when`(cache.getById(anyString())).thenReturn(Optional.empty())

        val returned = cache.hasBalance(null, player, currency, 100.0)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertFalse(returned)
    }

    @Test
    fun hasBalance_thenReturnTrue() {
        `when`(cache.getById(anyString())).thenReturn(Optional.of(wallet.setBalance(currency.id, 200.0)))

        val returned = cache.hasBalance(null, player, currency, 50.0)

        assertTrue(returned)
    }
}