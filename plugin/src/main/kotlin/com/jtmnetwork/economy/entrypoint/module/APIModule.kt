package com.jtmnetwork.economy.entrypoint.module

import com.google.inject.AbstractModule
import com.jtmnetwork.economy.entrypoint.api.TransactionAPI
import com.jtmnetwork.economy.entrypoint.api.TransactionAPIImpl
import com.jtmnetwork.economy.entrypoint.api.WalletAPI
import com.jtmnetwork.economy.entrypoint.api.WalletAPIImpl

class APIModule: AbstractModule() {
    override fun configure() {
        bind(WalletAPI::class.java).to(WalletAPIImpl::class.java)
        bind(TransactionAPI::class.java).to(TransactionAPIImpl::class.java)
    }
}