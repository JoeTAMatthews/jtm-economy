package com.jtmnetwork.economy.entrypoint.commands

import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.entrypoint.api.ExchangeAPI
import org.bukkit.entity.Player
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class ExchangeCommandsTest {

    private val exchangeAPI: ExchangeAPI = mock()
    private val localeMessenger: LocaleMessenger = mock()
    private val exchangeCommands = ExchangeCommands(exchangeAPI, localeMessenger)
    private val player: Player = mock()
    private val from: Currency = Currency(name = "test", abbreviation = "test", symbol = "£")
    private val to: Currency = Currency(name = "asd", abbreviation = "asd", symbol = "$")

    @Test
    fun onExchange() {
        `when`(exchangeAPI.exchange(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())).thenReturn(false)

        exchangeCommands.onExchange(player, from, to, 20.0)

        verify(exchangeAPI, times(1)).exchange(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(exchangeAPI)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(localeMessenger)
    }
}