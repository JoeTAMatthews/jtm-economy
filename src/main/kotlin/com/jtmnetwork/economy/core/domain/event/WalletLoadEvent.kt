package com.jtmnetwork.economy.core.domain.event

import com.jtm.framework.core.domain.model.event.FrameworkEvent
import com.jtmnetwork.economy.core.domain.entity.Wallet

class WalletLoadEvent(val wallet: Wallet): FrameworkEvent()