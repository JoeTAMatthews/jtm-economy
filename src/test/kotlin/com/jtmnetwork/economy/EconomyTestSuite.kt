package com.jtmnetwork.economy

import com.jtmnetwork.economy.data.worker.WalletLoaderTest
import com.jtmnetwork.economy.data.worker.WalletSaverTest
import com.jtmnetwork.economy.entrypoint.listener.PlayerListenerTest
import com.jtmnetwork.economy.entrypoint.listener.WalletListenerTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [
    WalletLoaderTest::class,
    WalletSaverTest::class,

    PlayerListenerTest::class,
    WalletListenerTest::class
])
class EconomyTestSuite