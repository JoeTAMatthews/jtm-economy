package com.jtmnetwork.economy.data.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.database.DatabaseConnector
import com.jtm.framework.data.service.Service
import com.jtmnetwork.economy.core.domain.entity.ExchangeRate
import java.util.*

@Singleton
class ExchangeRateService @Inject constructor(framework: Framework, connector: DatabaseConnector): Service<ExchangeRate, UUID>(framework, connector, ExchangeRate::class.java)