package com.jtmnetwork.economy.core.domain.entity

import com.jtmnetwork.economy.core.usecase.converter.BalanceConverter
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "wallets")
data class Wallet(
    @Id @Column(nullable = false, length = 36) val id: String = "",
    val name: String = "",
    @Column(columnDefinition = "LONGTEXT") @Convert(converter = BalanceConverter::class) val balances: MutableMap<UUID, Double> = HashMap(),
    val created: Long = System.currentTimeMillis()) {

    fun hasBalance(currency: UUID): Boolean {
        return balances.containsKey(currency)
    }

    fun getBalance(currency: UUID): Double {
        return balances[currency] ?: 0.0
    }

    fun addBalance(currency: UUID): Wallet {
        balances[currency] = 0.0
        return this
    }

    fun addBalance(currency: UUID, amount: Double): Wallet? {
        if (!hasBalance(currency)) return null
        val balance = balances[currency] ?: return null
        balances[currency] = balance.plus(amount)
        return this
    }

    fun setBalance(currency: UUID, amount: Double): Wallet {
        this.balances[currency] = amount
        return this
    }

    fun removeBalance(id: UUID): Wallet {
        balances.remove(id)
        return this
    }

    fun removeBalance(currency: UUID, amount: Double): Wallet? {
        if (!hasBalance(currency)) return null
        val balance = balances[currency] ?: return null
        balances[currency] = if (amount >= balance) 0.0 else balance.minus(amount)
        return this
    }
}
