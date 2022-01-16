package com.jtmnetwork.economy.core.domain.entity

import com.jtmnetwork.economy.core.domain.model.TransactionType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("transactions")
data class Transaction(@Id val id: UUID,
                       var type: TransactionType,
                       var playerId: UUID?,
                       var currency: UUID,
                       var amount: Double,
                       var balance: Double) {

    fun update(update: Transaction): Transaction {
        if (type != update.type) type = update.type
        if (playerId != update.playerId) playerId = update.playerId
        if (currency != update.currency) currency = update.currency
        if (amount != update.amount) amount = update.amount
        if (balance != update.balance) balance = update.balance
        return this
    }
}