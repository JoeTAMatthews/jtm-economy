package com.jtmnetwork.economy.entrypoint.listener

import com.jtm.framework.Framework
import com.jtmnetwork.economy.data.cache.WalletCache
import com.jtmnetwork.economy.data.service.WalletService
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class PlayerListenerTest {

    private val framework: Framework = mock()
    private val walletService: WalletService = mock()
    private val cache: WalletCache = mock()
    private val listener = PlayerListener(framework, walletService, cache)

    private val joinEvent: PlayerJoinEvent = mock()
    private val quitEvent: PlayerQuitEvent = mock()

    private val player: Player = mock()

    @Test
    fun onJoin() {
        `when`(joinEvent.player).thenReturn(player)

        listener.onJoin(joinEvent)

        verify(joinEvent, times(1)).player
        verifyNoMoreInteractions(joinEvent)

        verify(framework, times(1)).getLogging()
        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)
    }

    @Test
    fun onQuit() {
        `when`(quitEvent.player).thenReturn(player)

        listener.onQuit(quitEvent)

        verify(quitEvent, times(1)).player
        verifyNoMoreInteractions(quitEvent)

        verify(framework, times(1)).getLogging()
        verify(framework, times(1)).runTaskAsync(anyOrNull())
        verifyNoMoreInteractions(framework)
    }
}