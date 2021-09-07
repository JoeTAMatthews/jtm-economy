package com.jtmnetwork.economy.core.usecase.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.*
import javax.persistence.AttributeConverter

class BalanceConverter: AttributeConverter<MutableMap<UUID, Double>, String> {

    private val mapper = ObjectMapper().registerModule(KotlinModule())

    override fun convertToDatabaseColumn(attribute: MutableMap<UUID, Double>?): String {
        return mapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): MutableMap<UUID, Double> {
        val data = dbData ?: return mutableMapOf()
        return mapper.readValue(data)
    }
}