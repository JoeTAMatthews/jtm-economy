package com.jtmnetwork.economy.core.util

import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import java.util.*

class TestUtil {
    companion object {
        fun createCurrency(): Currency {
            return Currency(UUID.randomUUID(), "Pounds", "£")
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
    }
}