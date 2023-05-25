package com.jtmnetwork.economy.core.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("exchange_rates")
data class ExchangeRate(@Id val id: UUID,
                        var currency_from: UUID,
                        var currency_to: UUID,
                        var symbol: String,
                        var rate: Double,
                        val created: Long) {

    constructor(id: UUID, currency_from: UUID, currency_to: UUID, symbol: String, rate: Double): this(id, currency_from, currency_to, symbol, rate, System.currentTimeMillis());

    fun update(update: ExchangeRate): ExchangeRate {
        if (currency_from != update.currency_from) this.currency_from = update.currency_from
        if (currency_to != update.currency_to) this.currency_to = update.currency_to
        if (symbol != update.symbol) this.symbol = update.symbol
        if (rate != update.rate) this.rate = update.rate
        return this
    }
}