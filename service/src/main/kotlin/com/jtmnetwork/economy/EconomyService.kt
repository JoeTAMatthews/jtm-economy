package com.jtmnetwork.economy

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class EconomyService

fun main(args: Array<String>) {
    SpringApplication.run(EconomyService::class.java, *args)
}