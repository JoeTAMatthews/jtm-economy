package com.jtmnetwork.economy.entrypoint.api

import com.jtmnetwork.economy.core.domain.entity.Currency
import java.util.*

interface CurrencyAPI {

    fun getPrimaryCurrency(): Optional<Currency>

    fun getCurrency(id: UUID): Optional<Currency>

    fun getCurrency(name: String): Optional<Currency>

    fun getCurrencies(): List<Currency>
}