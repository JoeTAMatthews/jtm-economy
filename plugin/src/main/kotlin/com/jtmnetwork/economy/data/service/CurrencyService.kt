package com.jtmnetwork.economy.data.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.database.DatabaseConnector
import com.jtm.framework.data.service.Service
import com.jtmnetwork.economy.core.domain.entity.Currency
import java.util.*

@Singleton
class CurrencyService @Inject constructor(framework: Framework, connector: DatabaseConnector): Service<Currency, UUID>(framework, connector, Currency::class.java)