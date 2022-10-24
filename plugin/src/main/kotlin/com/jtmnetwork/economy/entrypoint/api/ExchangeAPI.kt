package com.jtmnetwork.economy.entrypoint.api

import com.jtmnetwork.economy.core.domain.entity.Currency
import org.bukkit.entity.Player

interface ExchangeAPI {

    /**
     * Request an exchange between currencies based on a set amount.
     *
     * @param player        the initiator of the exchange.
     * @param from          the currency you are exchanging from.
     * @param to            the currency you are exchange to.
     * @param amount        the amount of currency you are exchanging.
     *
     * @return              if the operation is successful return true, if not return false.
     */
    fun exchange(player: Player, from: Currency, to: Currency, amount: Double): Boolean
}