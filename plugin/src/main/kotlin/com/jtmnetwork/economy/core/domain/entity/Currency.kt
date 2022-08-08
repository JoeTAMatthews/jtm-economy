package com.jtmnetwork.economy.core.domain.entity

import com.jtm.framework.core.usecase.database.converter.UUIDConverter
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "currencies")
data class Currency(@Id @Convert(converter = UUIDConverter::class) @Column(length = 36) val id: UUID = UUID.randomUUID(),
                    var name: String = "",
                    var abbreviation: String = "",
                    var symbol: String = "",
                    var main: Boolean = false,
                    val created: Long = System.currentTimeMillis()) {

    fun info(): String {
        val builder = StringBuilder()
        builder.append("&7&m-----------------")
        builder.append("\n&fName: &e$name")
        builder.append("\n&fAbbreviation: &e$abbreviation")
        builder.append("\n&fSymbol: &e$symbol")
        builder.append("\n&fGlobal: &e$main")
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

    fun updateMain(): Currency {
        this.main = !this.main
        return this
    }

    fun getSymbolAmount(amount: Double): String {
        return "${symbol}${amount}"
    }

    fun getAbbreviationAmount(amount: Double): String {
        return "$amount $abbreviation"
    }
}
