package com.jtmnetwork.economy.entrypoint.controller

import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.data.service.TransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/transaction")
class TransactionController @Autowired constructor(private val transactionService: TransactionService) {

    @PostMapping
    fun postTransaction(@RequestBody transaction: Transaction): Mono<Transaction> {
        return transactionService.insertTransaction(transaction)
    }

    @PutMapping
    fun putTransaction(@RequestBody transaction: Transaction): Mono<Transaction> {
        return transactionService.updateTransaction(transaction)
    }

    @GetMapping("/{id}")
    fun getTransaction(@PathVariable id: UUID): Mono<Transaction> {
        return transactionService.getTransaction(id)
    }

    @GetMapping("/all")
    fun getTransactions(): Flux<Transaction> {
        return transactionService.getTransactions()
    }

    @DeleteMapping("/{id}")
    fun deleteTransaction(@PathVariable id: UUID): Mono<Transaction> {
        return transactionService.deleteTransaction(id)
    }
}