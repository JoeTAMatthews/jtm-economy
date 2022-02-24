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
import kotlin.collections.HashMap

@RunWith(MockitoJUnitRunner::class)
class CurrencyListenerTest {

    private val cache: WalletCache = mock()
    private val currencyListener = CurrencyListener(cache)

    private val currency = Currency(name = "test", abbreviation = "GBP", symbol = "£")
    private val walletOne = Wallet(UUID.randomUUID().toString(), "test")
    private val walletTwo = Wallet(UUID.randomUUID().toString(), "test", balances = mutableMapOf(currency.id to 0.0))
    private val addEvent: CurrencyAddEvent = mock()
    private val removeEvent: CurrencyRemoveEvent = mock()

    @Test
    fun onCurrencyAdd() {
        `when`(addEvent.currency).thenReturn(currency)
        `when`(cache.getAll()).thenReturn(listOf(walletOne))
        `when`(cache.update(anyOrNull(), anyOrNull())).thenReturn(walletOne)

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
        `when`(cache.getAll()).thenReturn(listOf(walletTwo))
        `when`(cache.update(anyOrNull(), anyOrNull())).thenReturn(walletTwo)

        currencyListener.onCurrencyRemove(removeEvent)

        verify(removeEvent, times(1)).currency
        verifyNoMoreInteractions(removeEvent)

        verify(cache, times(1)).getAll()
        verify(cache, times(1)).update(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(cache)
    }
}