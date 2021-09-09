package com.jtmnetwork.economy.data.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.database.DatabaseConnector
import com.jtm.framework.data.service.Service
import com.jtmnetwork.economy.core.domain.entity.Transaction
import java.util.*

@Singleton
class TransactionService @Inject constructor(framework: Framework, connector: DatabaseConnector): Service<Transaction, UUID>(framework, connector, Transaction::class.java)