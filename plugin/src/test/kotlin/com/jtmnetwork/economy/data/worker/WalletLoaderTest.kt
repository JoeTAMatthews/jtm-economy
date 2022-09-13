package com.jtmnetwork.economy.data.worker

import com.jtm.framework.Framework
import com.jtm.framework.core.util.Logging
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.service.WalletService
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
class WalletLoaderTest {

    private val framework: Framework = mock()
    private val logging: Logging = mock()
    private val service: WalletService = mock()
    private val player: Player = mock()
    private lateinit var loader: WalletLoader

    private val wallet = Wallet(UUID.randomUUID().toString(), "test")

    @Before
    fun setup() {
        `when`(framework.getLogging()).thenReturn(logging)
        `when`(player.uniqueId).thenReturn(UUID.randomUUID())
        `when`(player.name).thenReturn("test")

        loader = WalletLoader(framework, service, player)
    }

    @Test
    fun run_walletNotExists() {
        `when`(service.exists(anyOrNull())).thenReturn(false)
        `when`(service.insert(anyOrNull())).thenReturn(Optional.of(wallet))

        loader.run()

        verify(service, times(1)).exists(anyOrNull())
        verify(service, times(1)).insert(anyOrNull())
        verifyNoMoreInteractions(service)

        verify(player, times(2)).uniqueId
        verify(player, times(1)).name
        verifyNoMoreInteractions(player)

        verify(framework, times(1)).getLogging()
        verify(framework, times(1)).runTask(anyOrNull())
        verifyNoMoreInteractions(framework)
    }

    @Test
    fun run_walletExists() {
        `when`(service.exists(anyOrNull())).thenReturn(true)
        `when`(service.get(anyOrNull())).thenReturn(Optional.of(wallet))

        loader.run()

        verify(service, times(1)).exists(anyOrNull())
        verify(service, times(1)).get(anyOrNull())
        verifyNoMoreInteractions(service)

        verify(player, times(2)).uniqueId
        verifyNoMoreInteractions(player)

        verify(framework, times(1)).getLogging()
        verify(framework, times(1)).runTask(anyOrNull())
        verifyNoMoreInteractions(framework)
    }
}