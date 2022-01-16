package com.jtmnetwork.economy

import com.jtmnetwork.economy.data.service.CurrencyServiceTest
import com.jtmnetwork.economy.data.service.RateServiceTest
import com.jtmnetwork.economy.data.service.TransactionServiceTest
import com.jtmnetwork.economy.entrypoint.controller.CurrencyControllerTest
import com.jtmnetwork.economy.entrypoint.controller.RateControllerTest
import com.jtmnetwork.economy.entrypoint.controller.TransactionControllerTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [
    CurrencyServiceTest::class,
    CurrencyControllerTest::class,

    RateServiceTest::class,
    RateControllerTest::class,

    TransactionServiceTest::class,
    TransactionControllerTest::class
])
class EconomyServiceTestSuite