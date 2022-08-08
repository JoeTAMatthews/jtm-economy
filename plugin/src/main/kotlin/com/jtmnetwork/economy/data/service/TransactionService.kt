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

    /**
     * Retrieve all transactions based on the player being a receiver.
     *
     * @param id            the target identifier
     * @return              the list of transactions.
     */
    fun getByReceiver(id: UUID): List<Transaction> {
        return getAll()
                .stream()
                .filter { it.receiver == id && it.type == TransactionType.IN }
                .collect(Collectors.toList()) ?: listOf()
    }

    /**
     * Retrieve all transactions based on the player being a sender.
     *
     * @param id            the target identifier.
     * @return              the list of transactions.
     */
    fun getBySender(id: UUID): List<Transaction> {
        return getAll()
                .stream()
                .filter { it.sender == id && it.type == TransactionType.OUT }
                .collect(Collectors.toList()) ?: listOf()
    }

    /**
     * Retrieve all transactions based on the player being a receiver and sender.
     *
     * @param id            the target identifier
     * @return              the list of transactions.
     */
    fun getByReceiverAndSender(id: UUID): List<Transaction> {
        return getAll()
                .stream()
                .filter { it.receiver == id && it.type == TransactionType.IN || it.sender == id && it.type == TransactionType.OUT }
                .collect(Collectors.toList()) ?: listOf()
    }

    /**
     * Retrieve all transactions based on the currency.
     *
     * @param id            the target identifier
     * @param currency      the currency identifier
     * @return              the list of transactions.
     */
    fun getByCurrency(id: UUID, currency: UUID): List<Transaction> {
        return getAll()
                .stream()
                .filter { (it.receiver == id && it.type == TransactionType.IN || it.sender == id && it.type == TransactionType.OUT) && it.currency == currency }
                .collect(Collectors.toList()) ?: listOf()
    }

    /**
     * Convert all transactions and return a Stack based data structure.
     *
     * @param receiver      the identifier of the target
     * @return              the stack of indexed transactions.
     */
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