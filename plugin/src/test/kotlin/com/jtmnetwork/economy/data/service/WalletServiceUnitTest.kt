package com.jtmnetwork.economy.data.service

import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.database.DatabaseConnector
import com.jtm.framework.core.util.Logging
import com.jtm.framework.presenter.configuration.DatabaseConfiguration
import com.jtm.framework.presenter.configuration.RestConfiguration
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.core.util.TestUtil
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import java.io.File
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class WalletServiceUnitTest {

    private val framework: Framework = mock()
    private val logging: Logging = mock()
    private val messenger: LocaleMessenger = mock()
    private val connector: DatabaseConnector = mock()
    private val transService: TransactionService = mock()

    private lateinit var service: WalletService

    private val sender: CommandSender = mock()
    private val target: OfflinePlayer = mock()
    private val targetId = UUID.randomUUID()
    private val currency = TestUtil.createCurrency()
    private val wallet = Wallet(targetId.toString(), "Test")

    @Before
    fun setup() {
        val dbConf: DatabaseConfiguration = mock()
        val restConf: RestConfiguration = mock()

        `when`(framework.getRestConfiguration()).thenReturn(restConf)
        `when`(framework.dataFolder).thenReturn(File("./src/test/resources"))
        `when`(connector.logging).thenReturn(logging)
        `when`(connector.configuration).thenReturn(dbConf)
        `when`(framework.getLocaleMessenger()).thenReturn(messenger)
        `when`(framework.getLogging()).thenReturn(logging)

        service = spy(WalletService(framework, connector, transService))

        `when`(target.uniqueId).thenReturn(targetId)

        verify(framework, times(1)).getLocaleMessenger()
        verify(framework, times(1)).getLogging()
        verify(framework, times(1)).dataFolder
        verify(framework, times(1)).getRestConfiguration()

        verify(connector, times(1)).logging
        verify(connector, times(1)).configuration
    }

    @Test
    fun deposit_thenSendError_whenWalletEmpty() {
        `when`(service.get(anyString())).thenReturn(Optional.empty())

        val returned = service.deposit(sender, target, null, currency, 25.0)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verify(logging, times(1)).error(anyString())
        verifyNoMoreInteractions(logging)

        verifyNoMoreInteractions(framework)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun deposit_thenSendError_whenDepositFailed() {
        `when`(service.get(anyString())).thenReturn(Optional.of(wallet))

        val returned = service.deposit(sender, target, null, currency, 25.0)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verify(logging, times(1)).error(anyString())
        verifyNoMoreInteractions(logging)

        verifyNoMoreInteractions(framework)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun deposit_thenSendSuccess_whenDepositing() {
        `when`(service.get(anyString())).thenReturn(Optional.of(wallet.addBalance(currency.id)))

        val returned = service.deposit(sender, target, null, currency, 25.0)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString(), any())
        verifyNoMoreInteractions(messenger)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        assertTrue(returned.isPresent)

        returned.ifPresent { trans ->
            assertEquals(trans.amount, 25.0)
            assertEquals(trans.currency, currency.id)
        }
    }

    @Test
    fun withdraw_thenSendError_whenWalletEmpty() {
        `when`(service.get(anyString())).thenReturn(Optional.empty())

        val returned = service.withdraw(sender, target, null, currency, 34.0)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verify(logging, times(1)).error(anyString())
        verifyNoMoreInteractions(logging)

        verifyNoMoreInteractions(framework)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun withdraw_thenSendError_whenWithdrawFailed() {
        `when`(service.get(anyString())).thenReturn(Optional.of(wallet))

        val returned = service.withdraw(sender, target, null, currency, 25.0)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verify(logging, times(1)).error(anyString())
        verifyNoMoreInteractions(logging)

        verifyNoMoreInteractions(framework)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun withdraw_thenSendError_whenWithdrawing() {
        `when`(service.get(anyString())).thenReturn(Optional.of(wallet.addBalance(currency.id).addBalance(currency.id, 50.0)!!))

        val returned = service.withdraw(sender, target, null, currency, 10.0)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString(), any())
        verifyNoMoreInteractions(messenger)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        assertTrue(returned.isPresent)

        returned.ifPresent { trans ->
            assertEquals(trans.currency, currency.id)
            assertEquals(trans.amount, 10.0)
            assertEquals(trans.new_balance, 40.0)
        }
    }

    @Test
    fun balance_thenReturnEmpty_whenWalletEmpty() {
        `when`(service.get(anyString())).thenReturn(Optional.empty())

        val opt = service.balance(target, currency)

        verifyNoMoreInteractions(framework)
        assertTrue(opt.isEmpty)
    }

    @Test
    fun balance_thenReturnBalance() {
        `when`(service.get(anyString())).thenReturn(Optional.of(wallet.addBalance(currency.id)))

        val returned = service.balance(target, currency)

        verifyNoMoreInteractions(framework)

        assertTrue(returned.isPresent)

        returned.ifPresent { bal -> assertEquals(0.0, bal) }
    }

    @Test
    fun balanceSender_thenSendError_whenWalletEmpty() {
        `when`(service.get(anyString())).thenReturn(Optional.empty())

        val returned = service.balance(sender, target, currency)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verify(logging, times(1)).error(anyString())
        verifyNoMoreInteractions(logging)

        verifyNoMoreInteractions(framework)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun balanceSender_thenReturnBalance() {
        `when`(service.get(anyString())).thenReturn(Optional.of(wallet))

        val returned = service.balance(sender, target, currency)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString(), anyOrNull())
        verifyNoMoreInteractions(messenger)

        verifyNoMoreInteractions(framework)

        assertTrue(returned.isPresent)

        returned.ifPresent { bal -> assertEquals(0.0, bal) }
    }

    @Test
    fun hasBalanceSender_thenSendError_whenWalletEmpty() {
        `when`(service.get(anyString())).thenReturn(Optional.empty())

        val returned = service.hasBalance(sender, target, currency, 25.0)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verify(logging, times(1)).error(anyString())
        verifyNoMoreInteractions(logging)

        verifyNoMoreInteractions(framework)

        assertFalse(returned)
    }

    @Test
    fun hasBalanceSender_thenReturnTrue() {
        `when`(service.get(anyString())).thenReturn(Optional.of(wallet.setBalance(currency.id, 250.0)))

        val returned = service.hasBalance(sender, target, currency, 25.0)

        verifyNoMoreInteractions(framework)

        assertTrue(returned)
    }

    @Test
    fun hasBalance_thenSendError_whenWalletEmpty() {
        `when`(service.get(anyString())).thenReturn(Optional.empty())

        val returned = service.hasBalance(target, currency, 25.0)

        verify(logging, times(1)).warn(anyString())
        verify(logging, times(1)).error(anyString())
        verifyNoMoreInteractions(logging)

        verifyNoMoreInteractions(framework)

        assertFalse(returned)
    }

    @Test
    fun hasBalance_thenReturnTrue() {
        `when`(service.get(anyString())).thenReturn(Optional.of(wallet.setBalance(currency.id, 250.0)))

        val returned = service.hasBalance(target, currency, 25.0)

        verifyNoMoreInteractions(framework)

        assertTrue(returned)
    }
}