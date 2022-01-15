package com.jtmnetwork.economy.core.domain.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Exchange rate not found.")
class RateNotFound: RuntimeException()