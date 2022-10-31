package com.jtmnetwork.economy.data.service

import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.database.DatabaseConnector
import com.jtm.framework.core.util.Logging
import com.jtm.framework.presenter.configuration.DatabaseConfiguration
import com.jtm.framework.presenter.configuration.RestConfiguration
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.util.TestUtil
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.io.File
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class TransactionServiceUnitTest {

    private val logging: Logging = mock()
    private val messenger: LocaleMessenger = mock()
    private val framework: Framework = TestUtil.createFramework(messenger, logging)
    private val connector: DatabaseConnector = TestUtil.createConnector(logging)

    private lateinit var service: TransactionService

    private val id = UUID.randomUUID()

    private val sender: CommandSender = mock()
    private val offlinePlayer = TestUtil.createOfflinePlayer(id)
    private val player = TestUtil.createPlayer(id)
    private val currency = TestUtil.createCurrency()
    private val transList = TestUtil.createTransactionList(id, currency)

    @Before
    fun setup() {
        service = spy(TransactionService(framework, connector))

        TestUtil.verifyFramework(framework)
        TestUtil.verifyConnector(connector)
    }

    @Test
    fun getIngoingTransactionsOnlinePlayer_thenSendError_whenNoTransactions() {
        `when`(service.getAll()).thenReturn(emptyList())

        val returned = service.getIngoingTransactions(sender, player)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertEquals(0, returned.size)
    }

    @Test
    fun getIngoingTransactionsOnlinePlayer_thenReturnList() {
        `when`(service.getAll()).thenReturn(transList)

        val returned = service.getIngoingTransactions(sender, player)

        assertEquals(3, returned.size)

        assertEquals(TransactionType.IN, returned[0].type)
        assertEquals(15.0, returned[0].amount)

        assertEquals(TransactionType.IN, returned[1].type)
        assertEquals(25.0, returned[1].amount)

        assertEquals(TransactionType.IN, returned[2].type)
        assertEquals(35.0, returned[2].amount)
    }

    @Test
    fun getIngoingTransactionsOnlinePlayerByCurrency_thenSendError_whenNoTransactions() {
        `when`(service.getAll()).thenReturn(emptyList())

        val returned = service.getIngoingTransactions(sender, player, currency)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertEquals(0, returned.size)
    }

    @Test
    fun getIngoingTransactionsOnlinePlayerByCurrency_thenReturnList() {
        `when`(service.getAll()).thenReturn(transList)

        val returned = service.getIngoingTransactions(sender, player, currency)

        assertEquals(2, returned.size)

        assertEquals(TransactionType.IN, returned[0].type)
        assertEquals(15.0, returned[0].amount)

        assertEquals(TransactionType.IN, returned[1].type)
        assertEquals(25.0, returned[1].amount)
    }

    @Test
    fun getIngoingTransactionsOfflinePlayer_thenSendError_whenNoTransactions() {
        `when`(service.getAll()).thenReturn(emptyList())

        val returned = service.getIngoingTransactions(sender, offlinePlayer)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertEquals(0, returned.size)
    }

    @Test
    fun getIngoingTransactionsOfflinePlayer_thenReturnList() {
        `when`(service.getAll()).thenReturn(transList)

        val returned = service.getIngoingTransactions(sender, offlinePlayer)

        assertEquals(3, returned.size)

        assertEquals(TransactionType.IN, returned[0].type)
        assertEquals(15.0, returned[0].amount)

        assertEquals(TransactionType.IN, returned[1].type)
        assertEquals(25.0, returned[1].amount)

        assertEquals(TransactionType.IN, returned[2].type)
        assertEquals(35.0, returned[2].amount)
    }

    @Test
    fun getIngoingTransactionsOfflinePlayerByCurrency_thenSendError_whenNoTransactions() {
        `when`(service.getAll()).thenReturn(emptyList())

        val returned = service.getIngoingTransactions(sender, offlinePlayer, currency)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertEquals(0, returned.size)
    }

    @Test
    fun getIngoingTransactionsOfflinePlayerByCurrency_thenReturnList() {
        `when`(service.getAll()).thenReturn(transList)

        val returned = service.getIngoingTransactions(sender, offlinePlayer, currency)

        assertEquals(2, returned.size)

        assertEquals(TransactionType.IN, returned[0].type)
        assertEquals(15.0, returned[0].amount)

        assertEquals(TransactionType.IN, returned[1].type)
        assertEquals(25.0, returned[1].amount)
    }

    @Test
    fun getOutgoingTransactionsOnlinePlayer_thenSendError_whenNoTransactions() {
        `when`(service.getAll()).thenReturn(emptyList())

        val returned = service.getOutgoingTransactions(sender, player)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertEquals(0, returned.size)
    }

    @Test
    fun getOutgoingTransactionsOnlinePlayer_thenReturnList() {
        `when`(service.getAll()).thenReturn(transList)

        val returned = service.getOutgoingTransactions(sender, player)

        assertEquals(3, returned.size)
        assertEquals(TransactionType.OUT, returned[0].type)
        assertEquals(150.0, returned[0].amount)

        assertEquals(TransactionType.OUT, returned[1].type)
        assertEquals(230.0, returned[1].amount)

        assertEquals(TransactionType.OUT, returned[2].type)
        assertEquals(250.0, returned[2].amount)
    }

    @Test
    fun getOutgoingTransactionOnlinePlayerByCurrency_thenSendError_whenNoTransactions() {
        `when`(service.getAll()).thenReturn(emptyList())

        val returned = service.getOutgoingTransactions(sender, player, currency)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertEquals(0, returned.size)
    }

    @Test
    fun getOutgoingTransactionOnlinePlayerByCurrency_thenReturnList() {
        `when`(service.getAll()).thenReturn(transList)

        val returned = service.getOutgoingTransactions(sender, player, currency)

        assertEquals(2, returned.size)

        assertEquals(TransactionType.OUT, returned[0].type)
        assertEquals(150.0, returned[0].amount)

        assertEquals(TransactionType.OUT, returned[1].type)
        assertEquals(230.0, returned[1].amount)
    }

    @Test
    fun getOutgoingTransactionsOfflinePlayer_thenSendError_whenNoTransactions() {
        `when`(service.getAll()).thenReturn(emptyList())

        val returned = service.getOutgoingTransactions(sender, offlinePlayer)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertEquals(0, returned.size)
    }

    @Test
    fun getOutgoingTransactionsOfflinePlayer_thenReturnList() {
        `when`(service.getAll()).thenReturn(transList)

        val returned = service.getOutgoingTransactions(sender, offlinePlayer)

        assertEquals(3, returned.size)

        assertEquals(TransactionType.OUT, returned[0].type)
        assertEquals(150.0, returned[0].amount)

        assertEquals(TransactionType.OUT, returned[1].type)
        assertEquals(230.0, returned[1].amount)

        assertEquals(TransactionType.OUT, returned[2].type)
        assertEquals(250.0, returned[2].amount)
    }

    @Test
    fun getOutgoingTransactionsOfflinePlayerByCurrency_thenSendError_whenNoTransactions() {
        `when`(service.getAll()).thenReturn(emptyList())

        val returned = service.getOutgoingTransactions(sender, offlinePlayer, currency)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertEquals(0, returned.size)
    }

    @Test
    fun getOutgoingTransactionsOfflinePlayerByCurrency_thenReturnList() {
        `when`(service.getAll()).thenReturn(transList)

        val returned = service.getOutgoingTransactions(sender, offlinePlayer, currency)

        assertEquals(2, returned.size)

        assertEquals(TransactionType.OUT, returned[0].type)
        assertEquals(currency.id, returned[0].currency)
        assertEquals(150.0, returned[0].amount)

        assertEquals(TransactionType.OUT, returned[1].type)
        assertEquals(currency.id, returned[1].currency)
        assertEquals(230.0, returned[1].amount)
    }

    @Test
    fun getAllTransactionsOnlinePlayer_thenSendError_whenNoTransactions() {
        `when`(service.getAll()).thenReturn(emptyList())

        val returned = service.getAllTransactions(sender, player)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertEquals(0, returned.size)
    }

    @Test
    fun getAllTransactionsOnlinePlayer_thenReturnList() {
        `when`(service.getAll()).thenReturn(transList)

        val returned = service.getAllTransactions(sender, player)

        assertEquals(6, returned.size)

        assertEquals(TransactionType.IN, returned[0].type)
        assertEquals(15.0, returned[0].amount)

        assertEquals(TransactionType.IN, returned[1].type)
        assertEquals(25.0, returned[1].amount)

        assertEquals(TransactionType.IN, returned[2].type)
        assertEquals(35.0, returned[2].amount)
    }

    @Test
    fun getAllTransactionsOnlinePlayerByCurrency_thenSendError_whenNoTransactions() {
        `when`(service.getAll()).thenReturn(emptyList())

        val returned = service.getAllTransactions(sender, player, currency)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertEquals(0, returned.size)
    }

    @Test
    fun getAllTransactionsOnlinePlayerByCurrency_thenReturnList() {
        `when`(service.getAll()).thenReturn(transList)

        val returned = service.getAllTransactions(sender, player, currency)

        assertEquals(4, returned.size)

        assertEquals(TransactionType.IN, returned[0].type)
        assertEquals(15.0, returned[0].amount)

        assertEquals(TransactionType.IN, returned[1].type)
        assertEquals(25.0, returned[1].amount)
    }

    @Test
    fun getAllTransactionsOfflinePlayer_thenSendError_whenNoTransactions() {
        `when`(service.getAll()).thenReturn(emptyList())

        val returned = service.getAllTransactions(sender, offlinePlayer)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertEquals(0, returned.size)
    }

    @Test
    fun getAllTransactionsOfflinePlayer_thenReturnList() {
        `when`(service.getAll()).thenReturn(transList)

        val returned = service.getAllTransactions(sender, offlinePlayer)

        assertEquals(6, returned.size)

        assertEquals(TransactionType.IN, returned[0].type)
        assertEquals(15.0, returned[0].amount)

        assertEquals(TransactionType.IN, returned[1].type)
        assertEquals(25.0, returned[1].amount)

        assertEquals(TransactionType.IN, returned[2].type)
        assertEquals(35.0, returned[2].amount)
    }

    @Test
    fun getAllTransactionsOfflinePlayerByCurrency_thenSendError_whenNoTransactions() {
        `when`(service.getAll()).thenReturn(emptyList())

        val returned = service.getAllTransactions(sender, offlinePlayer, currency)

        verify(messenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertEquals(0, returned.size)
    }

    @Test
    fun getAllTransactionsOfflinePlayerByCurrency_thenReturnList() {
        `when`(service.getAll()).thenReturn(transList)

        val returned = service.getAllTransactions(sender, offlinePlayer, currency)

        assertEquals(4, returned.size)

        assertEquals(TransactionType.IN, returned[0].type)
        assertEquals(15.0, returned[0].amount)

        assertEquals(TransactionType.IN, returned[1].type)
        assertEquals(25.0, returned[1].amount)
    }
}