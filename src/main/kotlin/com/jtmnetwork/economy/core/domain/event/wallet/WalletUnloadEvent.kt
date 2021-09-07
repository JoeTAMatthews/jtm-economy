package com.jtmnetwork.economy.core.domain.event.wallet

import com.jtm.framework.core.domain.model.event.FrameworkEvent
import com.jtmnetwork.economy.core.domain.entity.Wallet

class WalletUnloadEvent(val wallet: Wallet): FrameworkEvent()