package com.jtmnetwork.economy.core.usecase.converter

import com.jtmnetwork.economy.core.domain.constants.TransactionType
import java.util.*
import javax.persistence.AttributeConverter

class TransactionTypeConverter: AttributeConverter<TransactionType, String> {
    override fun convertToDatabaseColumn(type: TransactionType?): String {
        return type!!.name
    }

    override fun convertToEntityAttribute(attribute: String?): TransactionType {
        return TransactionType.valueOf(attribute!!.uppercase(Locale.getDefault()))
    }
}