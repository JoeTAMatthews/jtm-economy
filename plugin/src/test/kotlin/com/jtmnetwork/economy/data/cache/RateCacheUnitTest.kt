package com.jtmnetwork.economy.data.cache

import com.jtm.framework.core.util.Logging
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.ExchangeRate
import com.jtmnetwork.economy.core.domain.entity.Wallet
import com.jtmnetwork.economy.core.util.TestUtil
import com.jtmnetwork.economy.data.service.ExchangeRateService
import org.bukkit.entity.Player
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class RateCacheUnitTest {

    private val logging: Logging = mock()
    private val messenger: LocaleMessenger = mock()
    private val framework = TestUtil.createFrameworkCache(messenger, logging)
    private val connector = TestUtil.createRedisConnector()
    private val service: ExchangeRateService = mock()

    private lateinit var cache: ExchangeRateCache

    private val player: Player = mock()
    private val rate = ExchangeRate(symbol = "GBPUSD", rate = 1.25)
    private val from = Currency(name = "Pounds", abbreviation = "GBP", symbol = "£")
    private val to = Currency(name = "Dollars", abbreviation = "USD", symbol = "$")
    private val wallet = Wallet(UUID.randomUUID().toString(), "JTM")

    @Before
    fun setup() {
        cache = spy(ExchangeRateCache(framework, service, connector))

        verify(framework, times(1)).getLocaleMessenger()
        verify(framework, times(1)).getLogging()
        verifyNoMoreInteractions(framework)
    }

    @Test
    fun getBySymbol_thenSendError_whenNotFound() {
        `when`(cache.getAll()).thenReturn(emptyList())

        val returned = cache.getBySymbol(player, "GBPUSD")

        verify(messenger, times(1)).sendMessage(player, "rate.not_found")
        verifyNoMoreInteractions(messenger)

        verify(logging, times(1)).warn(anyString())
        verifyNoMoreInteractions(logging)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun getBySymbol_thenReturnRate() {
        `when`(cache.getAll()).thenReturn(listOf(rate))

        val returned = cache.getBySymbol(player, "GBPUSD")

        assertTrue(returned.isPresent)

        returned.ifPresent {
            assertEquals(1.25, it.rate)
            assertEquals("GBPUSD", it.symbol)
        }
    }

    @Test
    fun exchange_thenSendError_whenInsufficientFunds() {
        `when`(cache.getBySymbol(anyOrNull(), anyString())).thenReturn(Optional.of(rate))

        val returned = cache.exchange(player, wallet.addBalance(from.id), from, to, 25.0)

        verify(messenger, times(1)).sendMessage(player, "user.insufficient_funds")
        verifyNoMoreInteractions(messenger)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun exchange_thenSendError_whenDepositedFailed() {
        `when`(cache.getBySymbol(anyOrNull(), anyString())).thenReturn(Optional.of(rate))

        val returned = cache.exchange(player, wallet.setBalance(from.id, 50.0), from, to, 25.0)

        verify(messenger, times(1)).sendMessage(player, "rate.failed.deposited")
        verifyNoMoreInteractions(messenger)

        assertTrue(returned.isEmpty)
    }

    @Test
    fun exchange_thenReturnWallet() {
        `when`(cache.getBySymbol(anyOrNull(), anyString())).thenReturn(Optional.of(rate))

        val returned = cache.exchange(player, wallet.setBalance(from.id, 50.0).addBalance(to.id), from, to, 50.0)

        assertTrue(returned.isPresent)

        returned.ifPresent {
            assertEquals("JTM", it.name)
            assertEquals(62.5, it.getBalance(to.id))
        }
    }
}
