package com.jtmnetwork.economy.core.domain.entity

import com.jtm.framework.core.usecase.database.converter.UUIDConverter
import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.usecase.converter.TransactionTypeConverter
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "transactions")
data class Transaction(@Id @Convert(converter = UUIDConverter::class) @Column(length = 36) val id: UUID = UUID.randomUUID(),
                       @Convert(converter = TransactionTypeConverter::class) val type: TransactionType,
                       @Convert(converter = UUIDConverter::class) @Column(nullable = true, length = 36) val playerId: UUID?,
                       @Convert(converter = UUIDConverter::class) @Column(length = 36) val currency: UUID,
                       val amount: Double,
                       val balance: Double)
