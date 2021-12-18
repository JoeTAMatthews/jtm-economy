package com.jtmnetwork.economy.entrypoint.commands

import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import org.bukkit.entity.Player
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class ExchangeCommandsTest {

    private val economyAPI: EconomyAPI = mock()
    private val localeMessenger: LocaleMessenger = mock()
    private val exchangeCommands = ExchangeCommands(economyAPI, localeMessenger)
    private val player: Player = mock()
    private val from: Currency = Currency(name = "test", abbreviation = "test", symbol = "£")
    private val to: Currency = Currency(name = "asd", abbreviation = "asd", symbol = "$")

    @Test
    fun onExchange() {
        `when`(economyAPI.exchangeAmount(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())).thenReturn(false)

        exchangeCommands.onExchange(player, from, to, 20.0)

        verify(economyAPI, times(1)).exchangeAmount(anyOrNull(), anyOrNull(), anyOrNull(), anyDouble())
        verifyNoMoreInteractions(economyAPI)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(localeMessenger)
    }
}