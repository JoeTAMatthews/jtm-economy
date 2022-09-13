package com.jtmnetwork.economy.data.worker

import com.jtm.framework.Framework
import com.jtm.framework.core.util.Logging
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
    private val logging: Logging = mock()
    private val service: WalletService = mock()
    private val cache: WalletCache = mock()
    private val player: Player = mock()

    private lateinit var walletSaver: WalletSaver

    private val wallet = Wallet(UUID.randomUUID().toString(), "test")

    @Before
    fun setup() {
        `when`(framework.getLogging()).thenReturn(logging)
        `when`(player.uniqueId).thenReturn(UUID.randomUUID())

        walletSaver = WalletSaver(framework, service, cache, player)
    }

    @Test
    fun run_thenCacheEmpty() {
        `when`(cache.getById(anyOrNull())).thenReturn(Optional.empty())

        walletSaver.run()

        verify(cache, times(1)).getById(anyOrNull())
        verifyNoMoreInteractions(cache)

        verifyNoInteractions(service)

        verify(framework, times(1)).getLogging()
        verifyNoMoreInteractions(framework)
    }

    @Test
    fun run_thenUpdateNull() {
        `when`(cache.getById(anyOrNull())).thenReturn(Optional.of(wallet))
        `when`(service.update(anyOrNull())).thenReturn(Optional.empty())

        walletSaver.run()

        verify(cache, times(1)).getById(anyOrNull())
        verifyNoMoreInteractions(cache)

        verify(service, times(1)).update(anyOrNull())
        verifyNoMoreInteractions(service)

        verify(framework, times(1)).getLogging()
        verifyNoMoreInteractions(framework)
    }

    @Test
    fun run() {
        `when`(cache.getById(anyOrNull())).thenReturn(Optional.of(wallet))
        `when`(service.update(anyOrNull())).thenReturn(Optional.of(wallet))

        walletSaver.run()

        verify(cache, times(1)).getById(anyOrNull())
        verifyNoMoreInteractions(cache)

        verify(service, times(1)).update(anyOrNull())
        verifyNoMoreInteractions(service)

        verify(framework, times(1)).getLogging()
        verify(framework, times(1)).runTask(anyOrNull())
        verifyNoMoreInteractions(framework)
    }
}