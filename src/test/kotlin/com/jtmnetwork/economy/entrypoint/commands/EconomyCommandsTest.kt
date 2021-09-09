package com.jtmnetwork.economy.entrypoint.commands

import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import org.bukkit.entity.Player
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

@RunWith(MockitoJUnitRunner::class)
class EconomyCommandsTest {

    private val economyAPI: EconomyAPI = mock()
    private val economyCommands = EconomyCommands(economyAPI)

    private val player: Player = mock()

    @Test
    fun onEcon() {
        economyCommands.onEcon(player)

        verify(player, times(1)).sendMessage(anyString())
        verifyNoMoreInteractions(player)
    }
}