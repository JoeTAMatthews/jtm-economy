package com.jtmnetwork.economy.core.domain.entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "currencies")
data class Currency(@Id val id: UUID = UUID.randomUUID(), var name: String, var abbreviation: String, var symbol: String) {

    fun info(): String {
        val builder = StringBuilder()
        builder.append("&7&m-----------------")
        builder.append("\n&fName: &e$name")
        builder.append("\n&fAbbreviation: &e$abbreviation")
        builder.append("\n&fSymbol: &e$symbol")
        builder.append("\n&7&m-----------------")
        return builder.toString()
    }

    fun updateName(name: String): Currency {
        this.name = name
        return this
    }

    fun updateAbbreviation(abbreviation: String): Currency {
        this.abbreviation = abbreviation
        return this
    }

    fun updateSymbol(symbol: String): Currency {
        this.symbol = symbol
        return this
    }
}
