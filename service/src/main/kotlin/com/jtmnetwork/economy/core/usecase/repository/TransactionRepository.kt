package com.jtmnetwork.economy.core.usecase.repository

import com.jtmnetwork.economy.core.domain.entity.Transaction
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionRepository: ReactiveMongoRepository<Transaction, UUID>