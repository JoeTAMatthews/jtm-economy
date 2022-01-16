package com.jtmnetwork.economy.core.domain.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Transaction not found.")
class TransactionNotFound: RuntimeException()