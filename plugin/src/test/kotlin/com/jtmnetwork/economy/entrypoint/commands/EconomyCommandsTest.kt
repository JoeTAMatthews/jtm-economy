package com.jtmnetwork.economy.entrypoint.commands

import com.jtm.framework.Framework
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class EconomyCommandsTest {

    private val framework: Framework = mock()
    private val economyAPI: EconomyAPI = mock()
    private val localeMessenger: LocaleMessenger = mock()
    private val economyCommands = EconomyCommands(framework, economyAPI, localeMessenger)

    private val player: Player = mock()
    private val target: OfflinePlayer = mock()
    private val currency = Currency(name = "test", abbreviation = "abb", symbol = "£")

    private val mockTrans: Transaction = mock()

    @Before
    fun setup() {
        `when`(target.player).thenReturn(player)
        `when`(target.name).thenReturn("test")
    }

    @Test
    fun onEcon() {
        economyCommands.onEcon(player)

        verify(player, times(1)).sendMessage(anyString())
        verifyNoMoreInteractions(player)
    }

    @Test
    fun onEconDepositOnline() {
        `when`(target.isOnline).thenReturn(true)
        `when`(economyAPI.deposit(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(mockTrans)

        economyCommands.onEconDeposit(player, target, currency, 5.0)

        verify(target, times(1)).isOnline
        verify(target, times(1)).name
        verify(target, times(1)).player
        verifyNoMoreInteractions(target)

        verify(economyAPI, times(1)).deposit(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(economyAPI)

        verify(localeMessenger, times(2)).sendMessage(anyOrNull(), anyString(), anyOrNull())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onEconDepositOnline_thenDepositFailed() {
        `when`(target.isOnline).thenReturn(true)
        `when`(economyAPI.deposit(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(null)

        economyCommands.onEconDeposit(player, target, currency, 5.0)

        verify(target, times(1)).isOnline
        verify(target, times(1)).player
        verifyNoMoreInteractions(target)

        verify(economyAPI, times(1)).deposit(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(economyAPI)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onEconDepositOffline() {
        economyCommands.onEconDeposit(player, target, currency, 5.0)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)

        verifyNoInteractions(economyAPI)
    }

    @Test
    fun onEconWithdrawOnline() {
        `when`(target.isOnline).thenReturn(true)
        `when`(economyAPI.withdraw(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(mockTrans)

        economyCommands.onEconWithdraw(player, target, currency, 5.0)

        verify(target, times(1)).isOnline
        verify(target, times(1)).name
        verify(target, times(1)).player
        verifyNoMoreInteractions(target)

        verify(economyAPI, times(1)).withdraw(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(economyAPI)

        verify(localeMessenger, times(2)).sendMessage(anyOrNull(), anyString(), anyOrNull())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onEconWithdrawOnline_thenDepositFailed() {
        `when`(target.isOnline).thenReturn(true)
        `when`(economyAPI.withdraw(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(null)

        economyCommands.onEconWithdraw(player, target, currency, 5.0)

        verify(target, times(1)).isOnline
        verify(target, times(1)).player
        verifyNoMoreInteractions(target)

        verify(economyAPI, times(1)).withdraw(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(economyAPI)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onEconWithdrawOffline() {
        economyCommands.onEconWithdraw(player, target, currency, 4.0)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)
    }

    @Test
    fun onEconBalanceOnline() {
        `when`(target.isOnline).thenReturn(true)
        `when`(economyAPI.balance(anyOrNull(), anyOrNull())).thenReturn(5.0)

        economyCommands.onEconBalance(player, target, currency)

        verify(target, times(1)).isOnline
        verify(target, times(1)).name
        verify(target, times(1)).player
        verifyNoMoreInteractions(target)

        verify(localeMessenger, times(1)).sendMessage(anyOrNull(), anyString(), anyOrNull())
        verifyNoMoreInteractions(localeMessenger)
    }

    @Test
    fun onEconBalanceOffline() {
        economyCommands.onEconBalance(player, target, currency)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)
    }
}