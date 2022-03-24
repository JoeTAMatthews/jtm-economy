package com.jtmnetwork.economy.core.domain.entity

import com.jtmnetwork.economy.core.domain.model.TransactionType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("transactions")
data class Transaction(@Id val id: UUID,
                       var type: TransactionType,
                       var sender: UUID?,
                       var receiver: UUID?,
                       var currency: UUID,
                       var amount: Double,
                       val previous_balance: Double = 0.0,
                       val new_balance: Double = 0.0,
                       val created: Long = 0) {

    fun update(update: Transaction): Transaction {
        if (type != update.type) type = update.type
        if (sender != update.sender) sender = update.sender
        if (receiver != update.receiver) receiver = update.receiver
        if (currency != update.currency) currency = update.currency
        if (amount != update.amount) amount = update.amount
        return this
    }
}