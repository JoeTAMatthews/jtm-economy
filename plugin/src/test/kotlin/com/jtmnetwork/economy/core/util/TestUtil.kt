package com.jtmnetwork.economy.core.util

import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.database.DatabaseConnector
import com.jtm.framework.core.usecase.redis.RedisConnector
import com.jtm.framework.core.util.Logging
import com.jtm.framework.presenter.configuration.DatabaseConfiguration
import com.jtm.framework.presenter.configuration.RestConfiguration
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.awt.Frame
import java.io.File
import java.util.*

class TestUtil {
    companion object {
        fun createCurrency(): Currency {
            return Currency(UUID.randomUUID(), "Pounds", "GBP", symbol = "£")
        }

        fun createTransactionList(id: UUID, currency: Currency): List<Transaction> {
            return listOf(
                Transaction(receiver = id, amount = 15.0, new_balance = 15.0, currency = currency.id),
                Transaction(receiver = id, amount = 25.0, new_balance = 25.0, currency = currency.id),
                Transaction(receiver = id, amount = 35.0, new_balance = 35.0),
                Transaction(sender = id, type = TransactionType.OUT, amount = 150.0, previous_balance = 175.0, new_balance = 25.0, currency = currency.id),
                Transaction(sender = id, type = TransactionType.OUT, amount = 230.0, previous_balance = 230.0, new_balance = 0.0, currency = currency.id),
                Transaction(sender = id, type = TransactionType.OUT, amount = 250.0, previous_balance = 300.0, new_balance = 50.0)
            )
        }

        fun createFramework(messenger: LocaleMessenger, logging: Logging): Framework {
            val restConf: RestConfiguration = mock()
            val framework: Framework = mock()

            `when`(framework.getRestConfiguration()).thenReturn(restConf)
            `when`(framework.dataFolder).thenReturn(File("./src/test/resources"))
            `when`(framework.getLocaleMessenger()).thenReturn(messenger)
            `when`(framework.getLogging()).thenReturn(logging)

            return framework
        }

        fun createFrameworkCache(messenger: LocaleMessenger, logging: Logging): Framework {
            val framework: Framework = mock()

            `when`(framework.getLocaleMessenger()).thenReturn(messenger)
            `when`(framework.getLogging()).thenReturn(logging)

            return framework
        }

        fun createConnector(logging: Logging): DatabaseConnector {
            val dbConf: DatabaseConfiguration = mock()
            val connector: DatabaseConnector = mock()

            `when`(connector.logging).thenReturn(logging)
            `when`(connector.configuration).thenReturn(dbConf)

            return connector
        }

        fun createRedisConnector(): RedisConnector {
            val redisConnector: RedisConnector = mock()

            return redisConnector
        }

        fun verifyFramework(framework: Framework) {
            verify(framework, times(1)).getLocaleMessenger()
            verify(framework, times(1)).getLogging()
            verify(framework, times(1)).dataFolder
            verify(framework, times(1)).getRestConfiguration()
        }

        fun verifyConnector(connector: DatabaseConnector) {
            verify(connector, times(1)).configuration
            verify(connector, times(1)).configuration
        }

        fun createCurrencyList(): List<Currency> {
            return listOf(
                Currency(name = "Pounds", abbreviation = "GBP", symbol = "£", main = true),
                Currency(name = "Dollars", abbreviation = "USD", symbol = "$")
            )
        }

        fun createPlayer(id: UUID): Player {
            val player: Player = mock()

            `when`(player.uniqueId).thenReturn(id)
            `when`(player.name).thenReturn("JTM")

            return player
        }

        fun createOfflinePlayer(id: UUID): OfflinePlayer {
            val player: OfflinePlayer = mock()

            `when`(player.uniqueId).thenReturn(id)
            `when`(player.name).thenReturn("JTM")

            return player
        }
    }
}