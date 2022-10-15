package com.jtmnetwork.economy.core.util

import com.jtmnetwork.economy.core.domain.entity.Currency
import java.util.*

class TestUtil {
    companion object {
        fun createCurrency(): Currency {
            return Currency(UUID.randomUUID(), "Pounds", "£")
        }
    }
}