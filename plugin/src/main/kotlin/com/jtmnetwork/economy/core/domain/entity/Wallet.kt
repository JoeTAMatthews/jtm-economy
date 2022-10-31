package com.jtmnetwork.economy.core.domain.entity

import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.usecase.converter.BalanceConverter
import org.bukkit.entity.Player
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "wallets")
data class Wallet(
    @Id @Column(nullable = false, length = 36) val id: String = "",
    val name: String = "",
    @Column(columnDefinition = "LONGTEXT") @Convert(converter = BalanceConverter::class) val balances: MutableMap<UUID, Double> = HashMap(),
    val created: Long = System.currentTimeMillis()) {

    constructor(player: Player): this(id = player.uniqueId.toString(), name = player.name)

    fun deposit(sender: UUID?, currency: UUID, amount: Double): Transaction? {
        val current = getBalance(currency)
        val deposited = addBalance(currency, amount) ?: return null
        return Transaction(type = TransactionType.IN, sender = sender, receiver = UUID.fromString(id), currency = currency, amount = amount, previous_balance = current, new_balance = deposited.getBalance(currency))
    }

    fun withdraw(receiver: UUID?, currency: UUID, amount: Double): Transaction? {
        val current = getBalance(currency)
        val withdrawn = removeBalance(currency, amount) ?: return null
        return Transaction(type = TransactionType.OUT, sender = UUID.fromString(id), receiver = receiver, currency = currency, amount = amount, previous_balance = current, new_balance = withdrawn.getBalance(currency))
    }

    fun hasBalance(currency: UUID): Boolean {
        return balances.containsKey(currency)
    }

    fun getBalance(currency: UUID): Double {
        return balances[currency] ?: 0.0
    }

    fun checkBalance(currency: UUID, amount: Double): Boolean {
        val balance = balances[currency] ?: return false
        return amount <= balance
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
