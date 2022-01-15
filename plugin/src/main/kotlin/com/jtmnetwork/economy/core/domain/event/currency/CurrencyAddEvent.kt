package com.jtmnetwork.economy.core.domain.event.currency

import com.jtm.framework.core.domain.model.event.FrameworkEvent
import com.jtmnetwork.economy.core.domain.entity.Currency

class CurrencyAddEvent(val currency: Currency): FrameworkEvent()