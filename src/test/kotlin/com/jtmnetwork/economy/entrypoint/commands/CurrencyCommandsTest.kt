package com.jtmnetwork.economy.entrypoint.commands

import com.jtm.framework.Framework
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.data.cache.CurrencyCache
import com.jtmnetwork.economy.data.service.CurrencyService
import org.bukkit.entity.Player
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class CurrencyCommandsTest {

    private val framework: Framework = mock()
    private val localeMessenger: LocaleMessenger = mock()
    private val cache: CurrencyCache = mock()
    private val service: CurrencyService = mock()
    private lateinit var currencyCommands: CurrencyCommands

    private val player: Player = mock()
    private val currency = Currency(name = "test",  abbreviation = "test", symbol = "£")

    @Before
    fun setup() {
        `when`(framework.getLocaleMessenger()).thenReturn(localeMessenger)

        currencyCommands = CurrencyCommands(framework, cache, service)
    }

    @Test
    fun onCurrency() {
        currencyCommands.onCurrency(player)

        verify(player, times(1)).sendMessage(anyString())
        verifyNoMoreInteractions(player)
    }

    @Test
    fun onCurrencyAdd_thenFound() {
        `when`(cache.existsByName(anyString())).thenReturn(true)

        currencyCommands.onCurrencyAdd(player, "test", "gbp", "£")

        verify(cache, times(1)).existsByName(anyString())
        verifyNoMoreInteractions(cache)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(localeMessenger)

        verify(framework, times(1)).getLocaleMessenger()
        verifyNoMoreInteractions(framework)
    }

    @Test
    fun onCurrencyAdd() {
        `when`(cache.existsByName(anyString())).thenReturn(false)
        `when`(cache.insert(anyOrNull(), anyOrNull())).thenReturn(currency)

        currencyCommands.onCurrencyAdd(player, "test", "gbp", "£")

        verify(cache, times(1)).existsByName(anyString())
        verify(cache, times(1)).insert(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(cache)

        verify(framework, times(1)).getLocaleMessenger()
        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verify(framework, times(1)).callEvent(anyOrNull())
        verifyNoMoreInteractions(framework)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString(), anyOrNull())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onCurrencyRemove_thenNotFound() {
        `when`(cache.existsByName(anyString())).thenReturn(false)

        currencyCommands.onCurrencyRemove(player, "test")

        verify(cache, times(1)).existsByName(anyString())
        verifyNoMoreInteractions(cache)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(localeMessenger)

        verify(framework, times(1)).getLocaleMessenger()
        verifyNoMoreInteractions(framework)
    }

    @Test
    fun onCurrencyRemove() {
        `when`(cache.existsByName(anyString())).thenReturn(true)
        `when`(cache.getByName(anyString())).thenReturn(currency)
        `when`(cache.remove(anyOrNull())).thenReturn(currency)

        currencyCommands.onCurrencyRemove(player, "test")

        verify(cache, times(1)).existsByName(anyString())
        verify(cache, times(1)).getByName(anyString())
        verify(cache, times(1)).remove(anyOrNull())
        verifyNoMoreInteractions(cache)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verify(framework, times(1)).getLocaleMessenger()
        verify(framework, times(1)).callEvent(anyOrNull())
        verifyNoMoreInteractions(framework)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString(), anyOrNull())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onCurrencyUpdate_thenNotFound() {
        `when`(cache.existsByName(anyString())).thenReturn(false)

        currencyCommands.onCurrencyUpdate(player, "test", "name", "new")

        verify(cache, times(1)).existsByName(anyString())
        verifyNoMoreInteractions(cache)

        verify(framework, times(1)).getLocaleMessenger()
        verifyNoMoreInteractions(framework)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onCurrencyUpdate_thenInvalidSetting() {
        `when`(cache.existsByName(anyString())).thenReturn(true)
        `when`(cache.getByName(anyString())).thenReturn(currency)

        currencyCommands.onCurrencyUpdate(player, "test", "valid", "value")

        verify(cache, times(1)).existsByName(anyString())
        verify(cache, times(1)).getByName(anyString())
        verifyNoMoreInteractions(cache)

        verify(framework, times(1)).getLocaleMessenger()
        verifyNoMoreInteractions(framework)

        verify(player, times(1)).sendMessage(anyString())
        verifyNoMoreInteractions(player)
    }

    @Test
    fun onCurrencyUpdate() {
        `when`(cache.existsByName(anyString())).thenReturn(true)
        `when`(cache.getByName(anyString())).thenReturn(currency)
        `when`(cache.update(anyOrNull(), anyOrNull())).thenReturn(currency.updateName("new"))

        currencyCommands.onCurrencyUpdate(player, "test", "name", "new")

        verify(cache, times(1)).existsByName(anyString())
        verify(cache, times(1)).getByName(anyString())
        verify(cache, times(1)).update(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(cache)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verify(framework, times(1)).getLocaleMessenger()
        verifyNoMoreInteractions(framework)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString(), anyOrNull())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onCurrencyInfo_thenNotFound() {
        `when`(cache.existsByName(anyString())).thenReturn(false)

        currencyCommands.onCurrencyInfo(player, "test")

        verify(cache, times(1)).existsByName(anyString())
        verifyNoMoreInteractions(cache)

        verify(framework, times(1)).getLocaleMessenger()
        verifyNoMoreInteractions(framework)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onCurrencyInfo() {
        `when`(cache.existsByName(anyString())).thenReturn(true)
        `when`(cache.getByName(anyString())).thenReturn(currency)

        currencyCommands.onCurrencyInfo(player, "test")

        verify(cache, times(1)).existsByName(anyString())
        verify(cache, times(1)).getByName(anyString())
        verifyNoMoreInteractions(cache)

        verify(framework, times(1)).getLocaleMessenger()
        verifyNoMoreInteractions(framework)

        verify(player, times(1)).sendMessage(anyString())
        verifyNoMoreInteractions(player)
    }

    @Test
    fun onCurrencyList() {
        `when`(cache.getAll()).thenReturn(listOf(currency))

        currencyCommands.onCurrencyList(player)

        verify(cache, times(1)).getAll()
        verifyNoMoreInteractions(cache)

        verify(framework, times(1)).getLocaleMessenger()
        verifyNoMoreInteractions(framework)

        verify(player, times(1)).sendMessage(anyString())
        verifyNoMoreInteractions(player)
    }
}