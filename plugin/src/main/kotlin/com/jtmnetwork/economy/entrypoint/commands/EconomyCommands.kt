package com.jtmnetwork.economy.entrypoint.commands

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.domain.annotations.Command
import com.jtm.framework.core.domain.annotations.Permission
import com.jtm.framework.core.domain.annotations.SubCommand
import com.jtm.framework.core.domain.annotations.Usage
import com.jtm.framework.core.util.UtilString
import com.jtm.framework.presenter.locale.LocaleMessenger
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import com.jtmnetwork.economy.entrypoint.api.TransactionAPI
import com.jtmnetwork.economy.entrypoint.api.WalletAPI
import okhttp3.internal.format
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

@Singleton
class EconomyCommands @Inject constructor(private val framework: Framework, private val walletAPI: WalletAPI, private val localeMessenger: LocaleMessenger) {

    private val logging = framework.getLogging()

    /**
     * Shows all the commands under "/econ"
     *
     * @param player        the command sender.
     */
    @Command("econ")
    @Permission(["econ.admin"])
    fun onEcon(player: Player) {
        val builder = StringBuilder()
        builder.append("&b&m                               ")
        builder.append("\n&eEconomy Commands")
        builder.append("\n&f- &c/econ deposit <player> <currency> <amount>")
        builder.append("\n&f- &c/econ withdraw <player> <currency> <amount>")
        builder.append("\n&f- &c/econ balance <player> <currency>")
        builder.append("\n&f- &c/econ help")
        builder.append("\n&b&m                               ")

        player.sendMessage(UtilString.colour(builder.toString()))
    }

    /**
     * Deposits a currency amount to the target player.
     *
     * @param player        the command sender.
     * @param target        the target player.
     * @param currency      the currency to be used.
     * @param amount        the amount of currency to be deposited.
     */
    @Command("econ")
    @SubCommand("deposit")
    @Usage("/econ deposit <player> <currency> <amount>")
    @Permission(["econ.admin"])
    fun onEconDeposit(player: Player, target: OfflinePlayer, currency: Currency, amount: Double) {
        when (target.isOnline) {
            true -> {
                val targetPlayer = target.player
                if (targetPlayer == null) {
                    player.sendMessage(UtilString.colour("&4Error: &cTarget player not online."))
                    return
                }

                val optTrans = walletAPI.deposit(player, targetPlayer, null, currency, amount)
                if (optTrans.isPresent)
                    logging.info(format("%s has successfully deposited %s in %s's wallet.", player.name, currency.getSymbolAmount(amount), targetPlayer.name))
                else
                    logging.info(format("%s has failed to deposit %s in %s's wallet.", player.name, currency.getSymbolAmount(amount), targetPlayer.name))
            }

            false -> {
                val optTrans = walletAPI.deposit(player, target, null, currency, amount)
                if (optTrans.isPresent)
                    logging.info(format("%s has successfully deposited %s in %s's wallet.", player.name, currency.getSymbolAmount(amount), target.name ?: target.uniqueId))
                else
                    logging.info(format("%s has failed to deposit %s in %s's wallet.", player.name, currency.getSymbolAmount(amount), target.name ?: target.uniqueId))
            }
        }
    }

    /**
     * Withdraw a currency amount from the target player.
     *
     * @param player        the command sender.
     * @param target        the target player.
     * @param currency      the currency to be used.
     * @param amount        the amount of currency to be withdrawn.
     */
    @Command("econ")
    @SubCommand("withdraw")
    @Usage("/econ withdraw <player> <currency> <amount>")
    @Permission(["econ.admin"])
    fun onEconWithdraw(player: Player, target: OfflinePlayer, currency: Currency, amount: Double) {
        when(target.isOnline) {
            true -> {
                val targetPlayer = target.player
                if (targetPlayer == null) {
                    player.sendMessage(UtilString.colour("&4Error: &cTarget player not online."))
                    return
                }

                val optTrans = walletAPI.withdraw(player, targetPlayer, null, currency, amount)
                if (optTrans.isPresent)
                    logging.info(format("%s has successfully withdrew %s from %s's wallet.", player.name, currency.getSymbolAmount(amount), targetPlayer.name))
                else
                    logging.info(format("%s has failed to withdraw %s from %s's wallet.", player.name, currency.getSymbolAmount(amount), targetPlayer.name))
            }

            false -> {
                val optTrans = walletAPI.withdraw(player, target, null, currency, amount)
                if (optTrans.isPresent)
                    logging.info(format("%s has successfully withdrew %s from %s's wallet.", player.name, currency.getSymbolAmount(amount), target.name ?: target.uniqueId))
                else
                    logging.info(format("%s has failed to withdraw %s from %s's wallet.", player.name, currency.getSymbolAmount(amount), target.name ?: target.uniqueId))
            }
        }
    }

    /**
     * Retrieve the balance of currency under a target player's wallet.
     *
     * @param player        the command sender.
     * @param target        the target player.
     * @param currency      the currency to be used.
     */
    @Command("econ")
    @SubCommand("balance")
    @Usage("/econ balance <player> <currency>")
    @Permission(["econ.admin"])
    fun onEconBalance(player: Player, target: OfflinePlayer, currency: Currency) {
        when(target.isOnline) {
            true -> {
                val targetPlayer = target.player
                if (targetPlayer == null) {
                    player.sendMessage(UtilString.colour("&4Error: &cTarget player not online."))
                    return
                }

                walletAPI.balance(player, targetPlayer, currency)
            }

            false -> {
                framework.runTaskAsync { walletAPI.balance(player, target, currency) }
            }
        }
    }

    /**
     * Shows commands for all the economy commands.
     *
     * @param player        the command sender.
     */
    @Command("econ")
    @SubCommand("help")
    @Usage("/econ help")
    @Permission(["econ.admin"])
    fun onEconHelp(player: Player) {
        val builder = StringBuilder()

        builder.append("&b&m                               ")
        builder.append("\n&aGeneral Commands")
        builder.append("\n&f- &e/currency &f- &bList of commands for currencies.")
        builder.append("\n&f- &e/erate &f- &bList of commands for exchange rate.")
        builder.append("\n&f- &e/exchange &f- &bExchange currencies between each other.")
        builder.append("\n&f- &e/wallet &f- &bOpen your personal wallet.")
        builder.append("\n&f- &e/rollback &f- &bRollback a player's wallet to a certain transaction level.")
        builder.append("\n&f- &e/pay &f- Pay other player's.")
        builder.append("\n&b&m                               ")

        player.sendMessage(UtilString.colour(builder.toString()))
    }
}