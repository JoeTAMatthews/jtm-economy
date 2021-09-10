package com.jtmnetwork.economy

import com.jtmnetwork.economy.data.cache.WalletCacheTest
import com.jtmnetwork.economy.data.worker.WalletLoaderTest
import com.jtmnetwork.economy.data.worker.WalletSaverTest
import com.jtmnetwork.economy.entrypoint.api.EconomyAPIImplTest
import com.jtmnetwork.economy.entrypoint.commands.CurrencyCommandsTest
import com.jtmnetwork.economy.entrypoint.commands.EconomyCommandsTest
import com.jtmnetwork.economy.entrypoint.listener.PlayerListenerTest
import com.jtmnetwork.economy.entrypoint.listener.WalletListenerTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [
    WalletLoaderTest::class,
    WalletSaverTest::class,
    WalletCacheTest::class,

    PlayerListenerTest::class,
    WalletListenerTest::class,

    CurrencyCommandsTest::class,
    EconomyCommandsTest::class,

    EconomyAPIImplTest::class
])
class EconomyTestSuite