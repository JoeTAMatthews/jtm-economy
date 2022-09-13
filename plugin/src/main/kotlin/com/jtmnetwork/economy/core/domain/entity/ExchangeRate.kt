package com.jtmnetwork.economy.core.domain.entity

import com.jtm.framework.core.usecase.database.converter.UUIDConverter
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "exchange_rates")
data class ExchangeRate(@Id @Convert(converter = UUIDConverter::class) @Column(length = 36) val id: UUID = UUID.randomUUID(),
                        var currency_from: UUID = UUID.randomUUID(),
                        var currency_to: UUID = UUID.randomUUID(),
                        var symbol: String = "",
                        var rate: Double = 1.0,
                        val created: Long = System.currentTimeMillis()) {
    
    fun info(): String {
        val builder = StringBuilder()
        builder.append("&7&m                 ")
        builder.append("\n&fSymbol: &e$symbol")
        builder.append("\n&fRate: &e$rate")
        builder.append("\n&7&m                 ")
        return builder.toString()
    }

    fun updateRate(rate: Double): ExchangeRate {
        this.rate = rate
        return this
    }

    fun updateFrom(id: UUID, abbr: String, to: String): ExchangeRate {
        this.currency_from = id
        this.symbol = "$abbr$to"
        return this
    }

    fun updateTo(id: UUID, abbr: String, from: String): ExchangeRate {
        this.currency_to = id
        this.symbol = "$from$abbr"
        return this
    }
}