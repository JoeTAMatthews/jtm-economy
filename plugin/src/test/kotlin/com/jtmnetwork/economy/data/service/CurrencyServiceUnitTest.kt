package com.jtmnetwork.economy.data.service

import com.jtm.framework.core.util.Logging
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.util.TestUtil
import org.bukkit.command.CommandSender
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class CurrencyServiceUnitTest {

    private val messenger: LocaleMessenger = mock()
    private val logging: Logging = mock()
    private val framework = TestUtil.createFramework(messenger, logging)
    private val connector = TestUtil.createConnector(logging)

    private lateinit var service: CurrencyService

    private val sender: CommandSender = mock()
    private val currencyList = TestUtil.createCurrencyList()
    private val currency = TestUtil.createCurrency()

    @Before
    fun setup() {
        service = spy(CurrencyService(framework, connector))

        TestUtil.verifyFramework(framework)
        TestUtil.verifyConnector(connector)
    }

    @Test
    fun getPrimaryCurrency_thenSendError_whenNotFound() {
        `when`(service.getAll()).thenReturn(emptyList())

        val returned = service.getPrimaryCurrency(sender)

        verify(messenger, times(1)).sendMessage(sender, "currency.not_found")
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).debug(anyString())
        verifyNoMoreInteractions(logging)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun getPrimaryCurrency_thenReturnCurrency() {
        `when`(service.getAll()).thenReturn(currencyList)

        val returned = service.getPrimaryCurrency(sender)

        assertTrue(returned.isPresent)

        returned.ifPresent {
            assertEquals("Pounds", it.name)
            assertEquals("GBP", it.abbreviation)
            assertEquals("£", it.symbol)
        }
    }

    @Test
    fun getCurrencyById_thenSendError_whenNotFound() {
        `when`(service.get(anyOrNull())).thenReturn(Optional.empty())

        val returned = service.getCurrency(sender, UUID.randomUUID())

        verify(messenger, times(1)).sendMessage(sender, "currency.not_found")
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).debug(anyString())
        verify(logging, times(1)).error(anyString())
        verifyNoMoreInteractions(logging)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun getCurrencyById_thenReturnCurrency() {
        `when`(service.get(anyOrNull())).thenReturn(Optional.of(currency))

        val returned = service.getCurrency(sender, UUID.randomUUID())

        assertTrue(returned.isPresent)

        returned.ifPresent {
            assertEquals("Pounds", it.name)
            assertEquals("GBP", it.abbreviation)
            assertEquals("£", it.symbol)
        }
    }

    @Test
    fun getCurrencyByName_thenSendError_whenNotFound() {
        `when`(service.getAll()).thenReturn(currencyList)

        val returned = service.getCurrency(sender, "Pound")

        verify(messenger, times(1)).sendMessage(sender, "currency.not_found")
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).debug(anyString())
        verifyNoMoreInteractions(logging)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun getCurrencyByName_thenReturnCurrency() {
        `when`(service.getAll()).thenReturn(currencyList)

        val returned = service.getCurrency(sender, "Pounds")

        assertTrue(returned.isPresent)

        returned.ifPresent {
            assertEquals("Pounds", it.name)
            assertEquals("GBP", it.abbreviation)
            assertEquals("£", it.symbol)
        }
    }

    @Test
    fun getCurrencies_thenReturnList() {
        `when`(service.getAll()).thenReturn(currencyList)

        val returned = service.getCurrencies()

        assertEquals(2, returned.size)

        assertEquals("Pounds", returned[0].name)
        assertEquals("GBP", returned[0].abbreviation)
        assertEquals("£", returned[0].symbol)

        assertEquals("Dollars", returned[1].name)
        assertEquals("USD", returned[1].abbreviation)
        assertEquals("$", returned[1].symbol)
    }
}