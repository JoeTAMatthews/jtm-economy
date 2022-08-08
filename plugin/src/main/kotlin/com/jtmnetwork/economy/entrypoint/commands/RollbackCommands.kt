package com.jtmnetwork.economy.entrypoint.commands

import com.google.inject.Inject
import com.jtm.framework.Framework
import com.jtm.framework.core.domain.annotations.Command
import com.jtm.framework.core.domain.annotations.Permission
import com.jtm.framework.core.domain.annotations.Usage
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class RollbackCommands @Inject constructor(private val framework: Framework, private val economyAPI: EconomyAPI, private val localeMessenger: LocaleMessenger) {

    /**
     * Rollback a target player's wallet transactions to a certain transaction index.
     *
     * @param player        the command sender.
     * @param target        the target player.
     * @param id            the selected transaction index.
     */
    @Command("rollback")
    @Usage("/rollback <target> <transaction_id>")
    @Permission(["economy.rollback"])
    fun onRollback(player: Player, target: OfflinePlayer, id: Int) {
        localeMessenger.sendMessage(player, "rollback.start", target.name)
        framework.runTaskAsync {
            when (target.isOnline) {
                true -> {
                    val targetPlayer = target.player
                    if (targetPlayer != null) {
                        if (economyAPI.processRollback(player, targetPlayer, id)) {
                            localeMessenger.sendMessage(player, "rollback.success", target.name)
                        } else localeMessenger.sendMessage(player, "rollback.failed")
                    }
                }
                false -> {
                    if (economyAPI.processRollback(player, target, id)) {
                        localeMessenger.sendMessage(player, "rollback.success", target.name)
                    } else localeMessenger.sendMessage(player, "rollback.failed")
                }
            }
            localeMessenger.sendMessage(player, "rollback.end", target.name)
        }
    }
}