package com.jtmnetwork.economy.core.domain.entity

import com.jtm.framework.core.usecase.database.converter.UUIDConverter
import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.usecase.converter.TransactionTypeConverter
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "transactions")
data class Transaction(@Id @Convert(converter = UUIDConverter::class) @Column(length = 36) val id: UUID = UUID.randomUUID(),
                       @Convert(converter = TransactionTypeConverter::class) val type: TransactionType = TransactionType.IN,
                       @Convert(converter = UUIDConverter::class) @Column(nullable = true, length = 36) val sender: UUID? = UUID.randomUUID(),
                       @Convert(converter = UUIDConverter::class) @Column(length = 36) val receiver: UUID = UUID.randomUUID(),
                       @Convert(converter = UUIDConverter::class) @Column(length = 36) val currency: UUID = UUID.randomUUID(),
                       val amount: Double = 0.0,
                       val balance: Double = 0.0,
                       val created: Long = System.currentTimeMillis())
