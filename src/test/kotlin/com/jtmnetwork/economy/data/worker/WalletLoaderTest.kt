package com.jtmnetwork.economy.data.worker

import com.jtm.framework.Framework
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
    private val service: WalletService = mock()
    private val player: Player = mock()
    private val loader = WalletLoader(framework, service, player)

    private val wallet = Wallet(UUID.randomUUID(), "test")

    @Before
    fun setup() {
        `when`(player.uniqueId).thenReturn(UUID.randomUUID())
        `when`(player.name).thenReturn("test")
    }

    @Test
    fun run_walletNotExists() {
        `when`(service.exists(anyString())).thenReturn(false)
        `when`(service.insert(anyOrNull())).thenReturn(wallet)

        loader.run()

        verify(service, times(1)).exists(anyString())
        verify(service, times(1)).insert(anyOrNull())
        verifyNoMoreInteractions(service)

        verify(player, times(2)).uniqueId
        verify(player, times(1)).name
        verifyNoMoreInteractions(player)

        verify(framework, times(1)).runTask(anyOrNull())
        verifyNoMoreInteractions(framework)
    }

    @Test
    fun run_walletExists() {
        `when`(service.exists(anyString())).thenReturn(true)
        `when`(service.get(anyString())).thenReturn(wallet)

        loader.run()

        verify(service, times(1)).exists(anyString())
        verify(service, times(1)).get(anyString())
        verifyNoMoreInteractions(service)

        verify(player, times(2)).uniqueId
        verifyNoMoreInteractions(player)

        verify(framework, times(1)).runTask(anyOrNull())
        verifyNoMoreInteractions(framework)
    }
}