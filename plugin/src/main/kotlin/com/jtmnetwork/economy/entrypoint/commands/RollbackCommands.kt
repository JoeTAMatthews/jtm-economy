package com.jtmnetwork.economy.entrypoint.commands

import com.google.inject.Inject
import com.jtm.framework.Framework
import com.jtm.framework.core.domain.annotations.Command
import com.jtm.framework.core.domain.annotations.Permission
import com.jtm.framework.core.domain.annotations.Usage
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import com.jtmnetwork.economy.entrypoint.api.UserAPI
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class RollbackCommands @Inject constructor(private val framework: Framework, private val userAPI: UserAPI, private val localeMessenger: LocaleMessenger) {

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
        when (target.isOnline) {
            true -> {
                val targetPlayer = target.player
                if (targetPlayer != null) userAPI.startRollback(player, targetPlayer, id)
            }

            false -> { userAPI.startRollback(player, target, id) }
        }
    }
}