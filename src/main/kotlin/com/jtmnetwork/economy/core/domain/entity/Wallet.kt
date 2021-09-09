package com.jtmnetwork.economy.core.domain.entity

import com.jtm.framework.core.usecase.database.converter.UUIDConverter
import com.jtmnetwork.economy.core.usecase.converter.BalanceConverter
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "wallets")
data class Wallet(
    @Id @Convert(converter = UUIDConverter::class) @Column(nullable = false, length = 36) val id: UUID,
    val name: String,
    @Column(columnDefinition = "LONGTEXT") @Convert(converter = BalanceConverter::class) val balances: MutableMap<UUID, Double> = HashMap(),
    val created: Long = System.currentTimeMillis()) {

    fun hasBalance(currency: Currency): Boolean {
        return balances.containsKey(currency.id)
    }

    fun getBalance(currency: UUID): Double {
        return balances[currency] ?: 0.0
    }

    fun addBalance(currency: Currency): Wallet {
        balances[currency.id] = 0.0
        return this
    }

    fun addBalance(currency: UUID, amount: Double): Wallet? {
        var balance = balances[currency] ?: return null
        balance += amount
        return this
    }

    fun removeBalance(id: UUID): Wallet {
        balances.remove(id)
        return this
    }

    fun removeBalance(currency: UUID, amount: Double): Wallet? {
        var balance = balances[currency] ?: return null
        balance += amount
        return this
    }
}
