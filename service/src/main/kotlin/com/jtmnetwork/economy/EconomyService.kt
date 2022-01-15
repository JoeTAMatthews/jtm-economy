package com.jtmnetwork.economy

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
open class EconomyService

fun main(args: Array<String>) {
    SpringApplication.run(EconomyService::class.java, *args)
}