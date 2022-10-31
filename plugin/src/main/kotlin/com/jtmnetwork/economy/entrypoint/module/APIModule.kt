package com.jtmnetwork.economy.entrypoint.module

import com.google.inject.AbstractModule
import com.jtmnetwork.economy.entrypoint.api.*
import com.jtmnetwork.economy.entrypoint.api.impl.*

class APIModule: AbstractModule() {
    override fun configure() {
        bind(WalletAPI::class.java).to(WalletAPIImpl::class.java)
        bind(TransactionAPI::class.java).to(TransactionAPIImpl::class.java)
        bind(UserAPI::class.java).to(UserAPIImpl::class.java)
        bind(CurrencyAPI::class.java).to(CurrencyAPIImpl::class.java)
        bind(ExchangeAPI::class.java).to(ExchangeAPIImpl::class.java)
    }
}