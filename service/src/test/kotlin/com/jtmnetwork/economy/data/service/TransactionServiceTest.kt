package com.jtmnetwork.economy.data.service

import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.exceptions.TransactionFound
import com.jtmnetwork.economy.core.domain.exceptions.TransactionNotFound
import com.jtmnetwork.economy.core.domain.model.TransactionType
import com.jtmnetwork.economy.core.usecase.repository.TransactionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class TransactionServiceTest {

    private val transactionRepository: TransactionRepository = mock()
    private val transactionService = TransactionService(transactionRepository)
    private val created = Transaction(UUID.randomUUID(), TransactionType.IN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 500.50, 50.50, 550.50, System.currentTimeMillis())

    @Test
    fun insertTransaction_thenFound() {
        `when`(transactionRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(created))

        val returned = transactionService.insertTransaction(created)

        verify(transactionRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(transactionRepository)

        StepVerifier.create(returned)
            .expectError(TransactionFound::class.java)
            .verify()
    }

    @Test
    fun insertTransaction() {
        `when`(transactionRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())
        `when`(transactionRepository.save(anyOrNull())).thenReturn(Mono.just(created))

        val returned = transactionService.insertTransaction(created)

        verify(transactionRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(transactionRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.sender).isEqualTo(created.sender)
                assertThat(it.receiver).isEqualTo(created.receiver)
                assertThat(it.currency).isEqualTo(created.currency)
                assertThat(it.previous_balance).isEqualTo(created.previous_balance)
                assertThat(it.new_balance).isEqualTo(created.new_balance)
                assertThat(it.amount).isEqualTo(created.amount)
            }
            .verifyComplete()
    }

    @Test
    fun updateTransaction_thenNotFound() {
        `when`(transactionRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = transactionService.updateTransaction(created)

        verify(transactionRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(transactionRepository)

        StepVerifier.create(returned)
            .expectError(TransactionNotFound::class.java)
            .verify()
    }

    @Test
    fun updateTransaction() {
        `when`(transactionRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(created))
        `when`(transactionRepository.save(anyOrNull())).thenReturn(Mono.just(created))

        val returned = transactionService.updateTransaction(created)

        verify(transactionRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(transactionRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.sender).isEqualTo(created.sender)
                assertThat(it.receiver).isEqualTo(created.receiver)
                assertThat(it.currency).isEqualTo(created.currency)
                assertThat(it.previous_balance).isEqualTo(created.previous_balance)
                assertThat(it.new_balance).isEqualTo(created.new_balance)
                assertThat(it.amount).isEqualTo(created.amount)
            }
            .verifyComplete()
    }

    @Test
    fun getTransaction_thenNotFound() {
        `when`(transactionRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = transactionService.getTransaction(UUID.randomUUID())

        verify(transactionRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(transactionRepository)

        StepVerifier.create(returned)
            .expectError(TransactionNotFound::class.java)
            .verify()
    }

    @Test
    fun getTransaction() {
        `when`(transactionRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(created))

        val returned = transactionService.getTransaction(UUID.randomUUID())

        verify(transactionRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(transactionRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.sender).isEqualTo(created.sender)
                assertThat(it.receiver).isEqualTo(created.receiver)
                assertThat(it.currency).isEqualTo(created.currency)
                assertThat(it.previous_balance).isEqualTo(created.previous_balance)
                assertThat(it.new_balance).isEqualTo(created.new_balance)
                assertThat(it.amount).isEqualTo(created.amount)
            }
            .verifyComplete()
    }

    @Test
    fun getTransactions() {
        `when`(transactionRepository.findAll()).thenReturn(Flux.just(created))

        val returned = transactionService.getTransactions()

        verify(transactionRepository, times(1)).findAll()
        verifyNoMoreInteractions(transactionRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.sender).isEqualTo(created.sender)
                assertThat(it.receiver).isEqualTo(created.receiver)
                assertThat(it.currency).isEqualTo(created.currency)
                assertThat(it.previous_balance).isEqualTo(created.previous_balance)
                assertThat(it.new_balance).isEqualTo(created.new_balance)
                assertThat(it.amount).isEqualTo(created.amount)
            }
            .verifyComplete()
    }

    @Test
    fun deleteTransaction_thenNotFound() {
        `when`(transactionRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = transactionService.deleteTransaction(UUID.randomUUID())

        verify(transactionRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(transactionRepository)

        StepVerifier.create(returned)
            .expectError(TransactionNotFound::class.java)
            .verify()
    }

    @Test
    fun deleteTransaction() {
        `when`(transactionRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(created))
        `when`(transactionRepository.delete(anyOrNull())).thenReturn(Mono.empty())

        val returned = transactionService.deleteTransaction(UUID.randomUUID())

        verify(transactionRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(transactionRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(created.id)
                assertThat(it.sender).isEqualTo(created.sender)
                assertThat(it.receiver).isEqualTo(created.receiver)
                assertThat(it.currency).isEqualTo(created.currency)
                assertThat(it.previous_balance).isEqualTo(created.previous_balance)
                assertThat(it.new_balance).isEqualTo(created.new_balance)
                assertThat(it.amount).isEqualTo(created.amount)
            }
            .verifyComplete()
    }
}