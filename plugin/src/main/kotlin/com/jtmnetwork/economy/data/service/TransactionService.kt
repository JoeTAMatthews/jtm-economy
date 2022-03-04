package com.jtmnetwork.economy.data.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.database.DatabaseConnector
import com.jtm.framework.data.service.Service
import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.model.TransactionNode
import java.util.*
import java.util.stream.Collectors

@Singleton
class TransactionService @Inject constructor(framework: Framework, connector: DatabaseConnector): Service<Transaction, UUID>(framework, connector, Transaction::class.java) {

    fun getByReceiver(id: UUID): List<Transaction> {
        return getAll()?.stream()?.filter { it.receiver == id }?.collect(Collectors.toList()) ?: listOf()
    }

    fun getByReceiverAndSender(id: UUID): List<Transaction> {
        return getAll()
                ?.stream()
                ?.filter { it.receiver == id && it.type == TransactionType.IN || it.sender == id && it.type == TransactionType.OUT }
                ?.collect(Collectors.toList()) ?: listOf()
    }

    fun getByReceiverAndCurrency(id: UUID, currency: UUID): List<Transaction> {
        return getAll()?.stream()?.filter { it.receiver == id && it.currency == currency }?.collect(Collectors.toList()) ?: listOf()
    }

    fun transactionsToStack(receiver: UUID): Stack<TransactionNode> {
        val stack: Stack<TransactionNode> = Stack()
        var index = 1
        getByReceiverAndSender(receiver)
                .sortedWith(Comparator.comparing(Transaction::created))
                .forEach {
                    stack.push(TransactionNode(index, it))
                    index++
                }
        return stack
    }
}