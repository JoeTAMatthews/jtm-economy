package com.jtmnetwork.economy.core.domain.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.FOUND, reason = "Wallet already found.")
class WalletFound: RuntimeException()