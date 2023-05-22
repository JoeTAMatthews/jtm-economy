package com.jtmnetwork.economy.entrypoint.commands

import com.jtm.framework.core.util.Logging
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.util.TestUtil
import com.jtmnetwork.economy.entrypoint.api.WalletAPI
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import java.util.*

@RunWith(MockitoJUnitRunner.Silent::class)
class JtmEconomyCommandsTest {

    private val messenger: LocaleMessenger = mock()
    private val logging: Logging = mock()
    private val framework = TestUtil.createFramework(messenger, logging)

    private val walletAPI: WalletAPI = mock()
    private val economyCommands = EconomyCommands(framework, walletAPI)

    private val player: Player = mock()

    @Mock
    private lateinit var target: OfflinePlayer


    private val targetPlayer: Player = mock()
    private val currency = spy(Currency(name = "test", abbreviation = "abb", symbol = "£"))

    private val trans: Transaction = mock()

    @Before
    fun setup() {
        `when`(target.player).thenReturn(targetPlayer)
        `when`(target.name).thenReturn("player_1")
        `when`(targetPlayer.name).thenReturn("player_1")

        `when`(player.name).thenReturn("player_2")

        verify(framework, times(1)).getLogging()
        verify(framework, times(1)).getLocaleMessenger()
        verifyNoMoreInteractions(framework)
    }

    @Test
    fun onEcon_shouldSendMessage() {
        economyCommands.onEcon(player)

        verify(player, times(1)).sendMessage(anyString())
        verifyNoMoreInteractions(player)
    }

    @Test
    fun onEconDeposit_shouldSendError_whenTargetIsNullAndOnline() {
        `when`(target.player).thenReturn(null)
        `when`(target.isOnline).thenReturn(true)

        economyCommands.onEconDeposit(player, target, currency, 25.0)

        verify(target, times(1)).isOnline
        verify(target, times(1)).player
        verifyNoMoreInteractions(target)

        verify(messenger, times(1)).sendMessage(player, "user.not_online")
        verifyNoMoreInteractions(messenger)

        verifyNoMoreInteractions(walletAPI)
        verifyNoMoreInteractions(logging)
    }

    @Test
    fun onEconDeposit_shouldSendSuccessLog_whenDepositingOnlineTarget() {
        `when`(target.isOnline).thenReturn(true)
        `when`(walletAPI.deposit(player, targetPlayer, null, currency, 50.0)).thenReturn(Optional.of(trans))

        economyCommands.onEconDeposit(player, target, currency, 50.0)

        verify(target, times(1)).isOnline
        verify(target, times(1)).player
        verifyNoMoreInteractions(target)

        verify(walletAPI, times(1)).deposit(player, targetPlayer, null, currency, 50.0)
        verifyNoMoreInteractions(walletAPI)

        verify(logging, times(1)).debug("player_2 has successfully deposited £50.0 in player_1's wallet.")
        verifyNoMoreInteractions(logging)

        verify(player, times(1)).name
        verifyNoMoreInteractions(player)

        verify(currency, times(1)).getSymbolAmount(50.0)
        verifyNoMoreInteractions(currency)
    }

    @Test
    fun onEconDeposit_shouldSendSuccessLog_whenDepositingOfflineTarget() {
        `when`(target.isOnline).thenReturn(false)
        `when`(walletAPI.deposit(player, target, null, currency, 100.0)).thenReturn(Optional.of(trans))

        economyCommands.onEconDeposit(player, target, currency, 100.0)

        verify(target, times(1)).isOnline
        verify(target, times(1)).name
        verifyNoMoreInteractions(target)

        verify(walletAPI, times(1)).deposit(player, target, null, currency, 100.0)
        verifyNoMoreInteractions(walletAPI)

        verify(logging, times(1)).debug("player_2 has successfully deposited £100.0 in player_1's wallet.")
        verifyNoMoreInteractions(logging)

        verify(player, times(1)).name
        verifyNoMoreInteractions(player)

        verify(currency, times(1)).getSymbolAmount(100.0)
        verifyNoMoreInteractions(currency)
    }

    @Test
    fun onEconWithdraw_shouldSendMessage_whenTargetIsOnlineAndNull() {
        `when`(target.isOnline).thenReturn(true)
        `when`(target.player).thenReturn(null)

        economyCommands.onEconWithdraw(player, target, currency, 125.0)

        verify(target, times(1)).isOnline
        verify(target, times(1)).player
        verifyNoMoreInteractions(target)

        verify(messenger, times(1)).sendMessage(player, "user.not_online")
        verifyNoMoreInteractions(messenger)

        verifyNoMoreInteractions(logging)
        verifyNoMoreInteractions(walletAPI)
    }

    @Test
    fun onEconWithdraw_shouldSendSuccessLog_whenWithdrawingOnlineTarget() {
        `when`(target.isOnline).thenReturn(true)
        `when`(walletAPI.withdraw(player, targetPlayer, null, currency, 125.0)).thenReturn(Optional.of(trans))

        economyCommands.onEconWithdraw(player, target, currency, 125.0)

        verify(target, times(1)).isOnline
        verify(target, times(1)).player
        verifyNoMoreInteractions(target)

        verify(logging, times(1)).debug("player_2 has successfully withdrew £125.0 from player_1's wallet.")
        verifyNoMoreInteractions(logging)

        verify(walletAPI, times(1)).withdraw(player, targetPlayer, null, currency, 125.0)
        verifyNoMoreInteractions(walletAPI)

        verify(player, times(1)).name
        verifyNoMoreInteractions(player)

        verify(currency, times(1)).getSymbolAmount(125.0)
        verifyNoMoreInteractions(currency)
    }

    @Test
    fun onEconWithdraw_shouldSendFailedLog_whenWithdrawingOnlineTarget() {
        `when`(target.isOnline).thenReturn(true)
        `when`(walletAPI.withdraw(player, targetPlayer, null, currency, 125.0)).thenReturn(Optional.empty())

        economyCommands.onEconWithdraw(player, target, currency, 125.0)

        verify(target, times(1)).isOnline
        verify(target, times(1)).player
        verifyNoMoreInteractions(target)

        verify(logging, times(1)).debug("player_2 has failed to withdraw £125.0 from player_1's wallet.")
        verifyNoMoreInteractions(logging)

        verify(walletAPI, times(1)).withdraw(player, targetPlayer, null, currency, 125.0)
        verifyNoMoreInteractions(walletAPI)

        verify(player, times(1)).name
        verifyNoMoreInteractions(player)

        verify(currency, times(1)).getSymbolAmount(125.0)
        verifyNoMoreInteractions(currency)
    }

    @Test
    fun onEconWithdraw_shouldSendSuccessLog_whenWithdrawingOfflineTarget() {
        `when`(target.isOnline).thenReturn(false)
        `when`(walletAPI.withdraw(player, target, null, currency, 125.0)).thenReturn(Optional.of(trans))

        economyCommands.onEconWithdraw(player, target, currency, 125.0)

        verify(target, times(1)).isOnline
        verify(target, times(1)).name
        verifyNoMoreInteractions(target)

        verify(logging, times(1)).debug("player_2 has successfully withdrew £125.0 from player_1's wallet.")
        verifyNoMoreInteractions(logging)

        verify(walletAPI, times(1)).withdraw(player, target, null, currency, 125.0)
        verifyNoMoreInteractions(walletAPI)

        verify(player, times(1)).name
        verifyNoMoreInteractions(player)

        verify(currency, times(1)).getSymbolAmount(125.0)
        verifyNoMoreInteractions(currency)
    }

    @Test
    fun onEconWithdraw_shouldSendFailedLog_whenWithdrawingOfflineTarget() {
        `when`(target.isOnline).thenReturn(false)
        `when`(walletAPI.withdraw(player, target, null, currency, 125.0)).thenReturn(Optional.empty())

        economyCommands.onEconWithdraw(player, target, currency, 125.0)

        verify(target, times(1)).isOnline
        verify(target, times(1)).name
        verifyNoMoreInteractions(target)

        verify(logging, times(1)).debug("player_2 has failed to withdraw £125.0 from player_1's wallet.")
        verifyNoMoreInteractions(logging)

        verify(walletAPI, times(1)).withdraw(player, target, null, currency, 125.0)
        verifyNoMoreInteractions(walletAPI)

        verify(player, times(1)).name
        verifyNoMoreInteractions(player)

        verify(currency, times(1)).getSymbolAmount(125.0)
        verifyNoMoreInteractions(currency)
    }

    @Test
    fun onEconBalance_shouldSendError_whenOnlineTargetIsNull() {
        `when`(target.isOnline).thenReturn(true)
        `when`(target.player).thenReturn(null)

        economyCommands.onEconBalance(player, target, currency)

        verify(target, times(1)).isOnline
        verify(target, times(1)).player
        verifyNoMoreInteractions(target)

        verify(messenger, times(1)).sendMessage(player, "user.not_online")
        verifyNoMoreInteractions(messenger)

        verifyNoMoreInteractions(walletAPI)
    }

    @Test
    fun onEconBalance_shouldCallBalance_whenTargetIsOnline() {
        `when`(target.isOnline).thenReturn(true)
        `when`(target.player).thenReturn(targetPlayer)

        economyCommands.onEconBalance(player, target, currency)

        verify(target, times(1)).isOnline
        verify(target, times(1)).player
        verifyNoMoreInteractions(target)

        verify(walletAPI, times(1)).balance(player, targetPlayer, currency)
        verifyNoMoreInteractions(walletAPI)
    }

    @Test
    fun onEconBalance_shouldCallBalance_whenTargetIsOffline() {
        `when`(target.isOnline).thenReturn(false)

        economyCommands.onEconBalance(player, target, currency)

        verify(target, times(1)).isOnline
        verifyNoMoreInteractions(target)

        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)
    }
}