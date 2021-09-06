package com.jtmnetwork.economy.data.worker

import com.jtm.framework.Framework
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.data.cache.WalletCache
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
import org.mockito.kotlin.verifyNoMoreInteractions
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class WalletSaverTest {

    private val framework: Framework = mock()
    private val service: WalletService = mock()
    private val cache: WalletCache = mock()
    private val player: Player = mock()

    private val walletSaver = WalletSaver(framework, service, cache, player)

    private val wallet = Wallet(UUID.randomUUID(), "test")

    @Before
    fun setup() {
        `when`(player.uniqueId).thenReturn(UUID.randomUUID())
    }

    @Test
    fun run_thenCacheNull() {
        `when`(cache.getById(anyOrNull())).thenReturn(null)

        walletSaver.run()

        verify(cache, times(1)).getById(anyOrNull())
        verifyNoMoreInteractions(cache)

        verifyNoInteractions(service)
        verifyNoInteractions(framework)
    }

    @Test
    fun run_thenUpdateNull() {
        `when`(cache.getById(anyOrNull())).thenReturn(wallet)
        `when`(service.update(anyOrNull())).thenReturn(null)

        walletSaver.run()

        verify(cache, times(1)).getById(anyOrNull())
        verifyNoMoreInteractions(cache)

        verify(service, times(1)).update(anyOrNull())
        verifyNoMoreInteractions(service)

        verifyNoInteractions(framework)
    }

    @Test
    fun run() {
        `when`(cache.getById(anyOrNull())).thenReturn(wallet)
        `when`(service.update(anyOrNull())).thenReturn(wallet)

        walletSaver.run()

        verify(cache, times(1)).getById(anyOrNull())
        verifyNoMoreInteractions(cache)

        verify(service, times(1)).update(anyOrNull())
        verifyNoMoreInteractions(service)

        verify(framework, times(1)).runTask(anyOrNull())
        verifyNoMoreInteractions(framework)
    }
}