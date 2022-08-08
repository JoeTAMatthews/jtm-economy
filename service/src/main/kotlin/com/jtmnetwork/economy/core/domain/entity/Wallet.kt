package com.jtmnetwork.economy.core.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("wallets")
data class Wallet(@Id val id: String,
                  var name: String,
                  var balances: MutableMap<UUID, Double> = HashMap(),
                  val created: Long) {

    fun update(update: Wallet): Wallet {
        if (name != update.name) name = update.name
        if (balances != update.balances) balances = update.balances
        return this
    }
}