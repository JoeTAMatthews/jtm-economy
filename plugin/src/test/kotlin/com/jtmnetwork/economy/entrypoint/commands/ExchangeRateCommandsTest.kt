package com.jtmnetwork.economy.entrypoint.commands

import com.jtm.framework.Framework
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.constants.ExchangeRateSetting
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.ExchangeRate
import com.jtmnetwork.economy.data.cache.ExchangeRateCache
import com.jtmnetwork.economy.data.service.ExchangeRateService
import org.bukkit.entity.Player
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ExchangeRateCommandsTest {

    private val framework: Framework = mock()
    private val localeMessenger: LocaleMessenger = mock()
    private val exchangeRateCache: ExchangeRateCache = mock()
    private val exchangeRateService: ExchangeRateService = mock()

    private lateinit var exchangeRateCommands: ExchangeRateCommands

    private val player: Player = mock()
    private val from = Currency(name = "Credits", abbreviation = "CRED", symbol = "$")
    private val to = Currency(name = "Points", abbreviation = "PTS", symbol = "£")
    private val rate = ExchangeRate(currency_from = from.id, currency_to = to.id, symbol = "${from.abbreviation}${to.abbreviation}", rate = 1.4)

    @Before
    fun setup() {
        `when`(framework.getLocaleMessenger()).thenReturn(localeMessenger)

        exchangeRateCommands = ExchangeRateCommands(framework, exchangeRateCache, exchangeRateService)
    }

    @Test
    fun onExchangeRate() {
        exchangeRateCommands.onExchangeRate(player)

        verify(player, times(1)).sendMessage(anyString())
        verifyNoMoreInteractions(player)
    }

    @Test
    fun onExchangeRateAdd_thenFound() {
        `when`(exchangeRateCache.existsBySymbol(anyString())).thenReturn(true)

        exchangeRateCommands.onExchangeRateAdd(player, from, to, 1.23)

        verify(exchangeRateCache, times(1)).existsBySymbol(anyString())
        verifyNoMoreInteractions(exchangeRateCache)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onExchangeRateAdd() {
        `when`(exchangeRateCache.existsBySymbol(anyString())).thenReturn(false)
        `when`(exchangeRateCache.insert(anyOrNull(), anyOrNull())).thenReturn(Optional.of(rate))

        exchangeRateCommands.onExchangeRateAdd(player, from, to, 1.34)

        verify(exchangeRateCache, times(1)).existsBySymbol(anyString())
        verify(exchangeRateCache, times(1)).insert(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(exchangeRateCache)

        verify(framework, times(1)).getLocaleMessenger()
        verify(framework, times(1)).runTaskAsync(anyOrNull())
//        verify(framework, times(1)).callEvent(anyOrNull())
        verifyNoMoreInteractions(framework)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString(), anyOrNull())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onExchangeRateRemove_thenNotFound() {
        `when`(exchangeRateCache.existsBySymbol(anyString())).thenReturn(false)

        exchangeRateCommands.onExchangeRateRemove(player, "test")

        verify(exchangeRateCache, times(1)).existsBySymbol(anyString())
        verifyNoMoreInteractions(exchangeRateCache)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onExchangeRateRemove() {
        `when`(exchangeRateCache.existsBySymbol(anyString())).thenReturn(true)
        `when`(exchangeRateCache.getBySymbol(anyString())).thenReturn(Optional.of(rate))
        `when`(exchangeRateCache.remove(anyOrNull())).thenReturn(Optional.of(rate))

        exchangeRateCommands.onExchangeRateRemove(player, "test")

        verify(exchangeRateCache, times(1)).existsBySymbol(anyString())
        verify(exchangeRateCache, times(1)).getBySymbol(anyString())
        verify(exchangeRateCache, times(1)).remove(anyOrNull())
        verifyNoMoreInteractions(exchangeRateCache)

        verify(framework, times(1)).getLocaleMessenger()
        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString(), anyOrNull())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onExchangeRateUpdate_thenNotFound() {
        `when`(exchangeRateCache.existsBySymbol(anyString())).thenReturn(false)

        exchangeRateCommands.onExchangeRateUpdate(player, "test", ExchangeRateSetting.RATE, "value")

        verify(exchangeRateCache, times(1)).existsBySymbol(anyString())
        verifyNoMoreInteractions(exchangeRateCache)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onExchangeRateUpdate() {
        `when`(exchangeRateCache.existsBySymbol(anyString())).thenReturn(true)
        `when`(exchangeRateCache.getBySymbol(anyString())).thenReturn(Optional.of(rate))
        `when`(exchangeRateCache.update(anyOrNull(), anyOrNull())).thenReturn(Optional.of(rate))

        exchangeRateCommands.onExchangeRateUpdate(player, "test", ExchangeRateSetting.RATE, "1.0")

        verify(exchangeRateCache, times(1)).existsBySymbol(anyString())
        verify(exchangeRateCache, times(1)).getBySymbol(anyString())
        verify(exchangeRateCache, times(1)).update(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(exchangeRateCache)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString(), anyOrNull())
        verifyNoMoreInteractions(localeMessenger)

        verify(framework, times(1)).getLocaleMessenger()
        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)
    }

    @Test
    fun onExchangeRateInfo_thenNotFound() {
        `when`(exchangeRateCache.existsBySymbol(anyString())).thenReturn(false)

        exchangeRateCommands.onExchangeRateInfo(player, "test")

        verify(exchangeRateCache, times(1)).existsBySymbol(anyString())
        verifyNoMoreInteractions(exchangeRateCache)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onExchangeRateInfo() {
        `when`(exchangeRateCache.existsBySymbol(anyString())).thenReturn(true)
        `when`(exchangeRateCache.getBySymbol(anyString())).thenReturn(Optional.of(rate))

        exchangeRateCommands.onExchangeRateInfo(player, "test")

        verify(exchangeRateCache, times(1)).existsBySymbol(anyString())
        verify(exchangeRateCache, times(1)).getBySymbol(anyString())
        verifyNoMoreInteractions(exchangeRateCache)

        verify(player, times(1)).sendMessage(anyString())
        verifyNoMoreInteractions(player)
    }

    @Test
    fun onExchangeRateList() {
        `when`(exchangeRateCache.getAll()).thenReturn(listOf(rate))

        exchangeRateCommands.onExchangeRateList(player)

        verify(exchangeRateCache, times(1)).getAll()
        verifyNoMoreInteractions(exchangeRateCache)

        verify(player, times(1)).sendMessage(anyString())
        verifyNoMoreInteractions(player)
    }
}