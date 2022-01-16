package com.jtmnetwork.economy.data.service

import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.exceptions.TransactionFound
import com.jtmnetwork.economy.core.domain.exceptions.TransactionNotFound
import com.jtmnetwork.economy.core.usecase.repository.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class TransactionService @Autowired constructor(private val transactionRepository: TransactionRepository) {

    fun insertTransaction(transaction: Transaction): Mono<Transaction> {
        return transactionRepository.findById(transaction.id)
            .flatMap<Transaction> { Mono.error(TransactionFound()) }
            .switchIfEmpty(Mono.defer { transactionRepository.save(transaction) })
    }

    fun updateTransaction(transaction: Transaction): Mono<Transaction> {
        return transactionRepository.findById(transaction.id)
            .switchIfEmpty(Mono.defer { Mono.error(TransactionNotFound()) })
            .flatMap { transactionRepository.save(it.update(transaction)) }
    }

    fun getTransaction(id: UUID): Mono<Transaction> {
        return transactionRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(TransactionNotFound()) })
    }

    fun getTransactions(): Flux<Transaction> {
        return transactionRepository.findAll()
    }

    fun deleteTransaction(id: UUID): Mono<Transaction> {
        return transactionRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(TransactionNotFound()) })
            .flatMap { transactionRepository.delete(it).thenReturn(it) }
    }
}