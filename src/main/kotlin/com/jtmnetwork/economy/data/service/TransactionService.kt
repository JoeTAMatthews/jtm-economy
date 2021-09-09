package com.jtmnetwork.economy.data.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.database.DatabaseConnector
import com.jtm.framework.data.service.Service
import com.jtmnetwork.economy.core.domain.entity.Transaction
import java.util.*
import java.util.stream.Collectors

@Singleton
class TransactionService @Inject constructor(framework: Framework, connector: DatabaseConnector): Service<Transaction, UUID>(framework, connector, Transaction::class.java) {

    fun getByPlayerId(id: UUID): List<Transaction> {
        return getAll()?.stream()?.filter { it.playerId == id }?.collect(Collectors.toList()) ?: listOf()
    }

    fun getByPlayerIdAndCurrency(id: UUID, currency: UUID): List<Transaction> {
        return getAll()?.stream()?.filter { it.playerId == id && it.currency == currency }?.collect(Collectors.toList()) ?: listOf()
    }
}