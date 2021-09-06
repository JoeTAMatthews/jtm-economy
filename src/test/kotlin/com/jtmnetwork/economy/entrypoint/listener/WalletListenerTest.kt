package com.jtmnetwork.economy.entrypoint.listener

import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.core.domain.event.WalletLoadEvent
import com.jtmnetwork.economy.core.domain.event.WalletUnloadEvent
import com.jtmnetwork.economy.data.cache.WalletCache
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
class WalletListenerTest {

    private val cache: WalletCache = mock()
    private val walletListener = WalletListener(cache)

    private val loadEvent: WalletLoadEvent = mock()
    private val unloadEvent: WalletUnloadEvent = mock()
    private val wallet = Wallet(UUID.randomUUID(), "test")

    @Test
    fun onLoad() {
        `when`(loadEvent.wallet).thenReturn(wallet)
        `when`(cache.insert(anyOrNull(), anyOrNull())).thenReturn(wallet)

        walletListener.onLoad(loadEvent)

        verify(loadEvent, times(1)).wallet
        verifyNoMoreInteractions(loadEvent)

        verify(cache, times(1)).insert(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(cache)
    }

    @Test
    fun onUnload() {
        `when`(unloadEvent.wallet).thenReturn(wallet)
        `when`(cache.remove(anyOrNull())).thenReturn(wallet)

        walletListener.onUnload(unloadEvent)

        verify(unloadEvent, times(1)).wallet
        verifyNoMoreInteractions(unloadEvent)

        verify(cache, times(1)).remove(anyOrNull())
        verifyNoMoreInteractions(cache)
    }
}