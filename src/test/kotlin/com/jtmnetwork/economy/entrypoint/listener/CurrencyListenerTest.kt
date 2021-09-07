package com.jtmnetwork.economy.entrypoint.listener

import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.core.domain.event.currency.CurrencyAddEvent
import com.jtmnetwork.economy.core.domain.event.currency.CurrencyRemoveEvent
import com.jtmnetwork.economy.data.cache.WalletCache
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class CurrencyListenerTest {

    private val cache: WalletCache = mock()
    private val currencyListener = CurrencyListener(cache)

    private val wallet = Wallet(UUID.randomUUID(), "test")
    private val currency = Currency(name = "test", abbreviation = "GBP", symbol = "£")
    private val addEvent: CurrencyAddEvent = mock()
    private val removeEvent: CurrencyRemoveEvent = mock()

    @Test
    fun onCurrencyAdd() {
        `when`(addEvent.currency).thenReturn(currency)
        `when`(cache.getAll()).thenReturn(listOf(wallet))
        `when`(cache.update(anyOrNull(), anyOrNull())).thenReturn(wallet)

        currencyListener.onCurrencyAdd(addEvent)

        verify(addEvent, times(1)).currency
        verifyNoMoreInteractions(addEvent)

        verify(cache, times(1)).getAll()
        verify(cache, times(1)).update(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(cache)
    }

    @Test
    fun onCurrencyRemove() {
        `when`(removeEvent.currency).thenReturn(currency)
        `when`(cache.getAll()).thenReturn(listOf(wallet))
        `when`(cache.update(anyOrNull(), anyOrNull())).thenReturn(wallet)

        currencyListener.onCurrencyRemove(removeEvent)

        verify(removeEvent, times(1)).currency
        verifyNoMoreInteractions(removeEvent)

        verify(cache, times(1)).getAll()
        verify(cache, times(1)).update(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(cache)
    }
}