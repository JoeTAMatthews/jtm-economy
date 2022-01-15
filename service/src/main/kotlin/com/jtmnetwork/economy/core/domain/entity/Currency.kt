package com.jtmnetwork.economy.core.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("currencies")
data class Currency(@Id val id: UUID, var name: String, var abbreviation: String, var symbol: String) {

    fun update(update: Currency): Currency {
        if (update.name != name) this.name = update.name
        if (update.abbreviation != abbreviation) this.abbreviation = update.abbreviation
        if (update.symbol != symbol) this.symbol = update.symbol
        return this
    }

}
