package com.jtmnetwork.economy

import com.jtmnetwork.economy.data.cache.RateCacheUnitTest
import com.jtmnetwork.economy.data.cache.WalletCacheUnitTest
import com.jtmnetwork.economy.data.service.CurrencyServiceUnitTest
import com.jtmnetwork.economy.data.service.TransactionServiceUnitTest
import com.jtmnetwork.economy.data.service.WalletServiceUnitTest
import com.jtmnetwork.economy.data.worker.WalletLoaderTest
import com.jtmnetwork.economy.data.worker.WalletSaverTest
import com.jtmnetwork.economy.entrypoint.commands.*
import com.jtmnetwork.economy.entrypoint.listener.CurrencyListenerTest
import com.jtmnetwork.economy.entrypoint.listener.PlayerListenerTest
import com.jtmnetwork.economy.entrypoint.listener.WalletListenerTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [
    WalletLoaderTest::class,
    WalletSaverTest::class,

    RateCacheUnitTest::class,
    WalletCacheUnitTest::class,

    PlayerListenerTest::class,
    WalletListenerTest::class,
    CurrencyListenerTest::class,

    CurrencyCommandsTest::class,
    JtmEconomyCommandsTest::class,
    ExchangeRateCommandsTest::class,
    ExchangeCommandsTest::class,

    CurrencyServiceUnitTest::class,
    TransactionServiceUnitTest::class,
    WalletServiceUnitTest::class
])
class JtmEconomyTestSuite