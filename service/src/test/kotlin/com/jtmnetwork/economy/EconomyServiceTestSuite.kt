package com.jtmnetwork.economy

import com.jtmnetwork.economy.data.service.CurrencyServiceTest
import com.jtmnetwork.economy.data.service.RateServiceTest
import com.jtmnetwork.economy.entrypoint.controller.CurrencyControllerTest
import com.jtmnetwork.economy.entrypoint.controller.RateControllerTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [
    CurrencyServiceTest::class,
    CurrencyControllerTest::class,

    RateServiceTest::class,
    RateControllerTest::class
])
class EconomyServiceTestSuite