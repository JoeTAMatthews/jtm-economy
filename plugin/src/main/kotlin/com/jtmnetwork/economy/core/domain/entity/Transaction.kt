package com.jtmnetwork.economy.core.domain.entity

import com.jtm.framework.core.usecase.database.converter.UUIDConverter
import com.jtm.framework.core.util.UtilString
import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.usecase.converter.TransactionTypeConverter
import com.jtmnetwork.economy.core.util.UtilTime
import com.jtmnetwork.economy.data.cache.CurrencyCache
import org.bukkit.Bukkit
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "transactions")
data class Transaction(@Id @Convert(converter = UUIDConverter::class) @Column(length = 36) val id: UUID = UUID.randomUUID(),
                       @Convert(converter = TransactionTypeConverter::class) val type: TransactionType = TransactionType.IN,
                       @Convert(converter = UUIDConverter::class) @Column(nullable = true, length = 36) val sender: UUID? = UUID.randomUUID(),
                       @Convert(converter = UUIDConverter::class) @Column(nullable = true, length = 36) val receiver: UUID? = UUID.randomUUID(),
                       @Convert(converter = UUIDConverter::class) @Column(length = 36) val currency: UUID = UUID.randomUUID(),
                       val amount: Double = 0.0,
                       val previous_balance: Double = 0.0,
                       val new_balance: Double = 0.0,
                       val created: Long = System.currentTimeMillis()) {

    fun toString(currencyCache: CurrencyCache): String {
        val builder = StringBuilder()

        val senderName = if (sender == null) "SERVER" else Bukkit.getOfflinePlayer(sender).name
        val receiverName = if (receiver == null) "SERVER" else Bukkit.getOfflinePlayer(receiver).name
        val opt = currencyCache.getById(currency)
        if (!opt.isPresent) return ""
        val currency = opt.get()

        builder.append("\n&7&m------------------------")
        builder.append("\n&fUniqueId: &b$id")
        builder.append("\n&fTransaction: &b${type.name}")
        builder.append("\n&fSender: &b$senderName")
        builder.append("\n&fReceiver: &b$receiverName")
        builder.append("\n&fCurrency: &b${currency.name}")
        builder.append("\n&fAmount: &b${currency.getAbbreviationAmount(amount)}")
        builder.append("\n&fPrevious Balance: &b${currency.getAbbreviationAmount(previous_balance)}")
        builder.append("\n&fNew Balance: &b${currency.getAbbreviationAmount(new_balance)}")
        builder.append("\n&fTime: &b${UtilTime.formatTime(created)}")
        builder.append("\n&7&m------------------------")

        return builder.toString()
    }

    fun toString(index: Int, currencyCache: CurrencyCache): String {
        val builder = StringBuilder()

        val senderName = if (sender == null) "SERVER" else Bukkit.getOfflinePlayer(sender).name
        val receiverName = if (receiver == null) "SERVER" else Bukkit.getOfflinePlayer(receiver).name
        val opt = currencyCache.getById(currency)
        if (!opt.isPresent) return ""
        val currency = opt.get()

        builder.append("\n&7&m------------------------")
        builder.append("\n&fId: &b$index")
        builder.append("\n&fUniqueId: &b$id")
        builder.append("\n&fTransaction: &b${type.name}")
        builder.append("\n&fSender: &b$senderName")
        builder.append("\n&fReceiver: &b$receiverName")
        builder.append("\n&fCurrency: &b${currency.name}")
        builder.append("\n&fAmount: &b${currency.getAbbreviationAmount(amount)}")
        builder.append("\n&fPrevious Balance: &b${currency.getAbbreviationAmount(previous_balance)}")
        builder.append("\n&fNew Balance: &b${currency.getAbbreviationAmount(new_balance)}")
        builder.append("\n&fTime: &b${UtilTime.formatTime(created)}")
        builder.append("\n&7&m------------------------")

        return builder.toString()
    }
}
