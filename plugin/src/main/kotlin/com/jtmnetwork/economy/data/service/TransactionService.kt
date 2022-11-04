package com.jtmnetwork.economy.data.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jtm.framework.Framework
import com.jtm.framework.core.usecase.database.DatabaseConnector
import com.jtm.framework.data.service.Service
import com.jtmnetwork.economy.core.domain.constants.TransactionType
import com.jtmnetwork.economy.core.domain.entity.Currency
import com.jtmnetwork.economy.core.domain.entity.Transaction
import com.jtmnetwork.economy.core.domain.model.TransactionNode
import okhttp3.internal.format
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.stream.Collectors
import kotlin.Comparator

@Singleton
class TransactionService @Inject constructor(framework: Framework, connector: DatabaseConnector): Service<Transaction, UUID>(framework, connector, Transaction::class.java) {

    private val logging = framework.getLogging()
    private val messenger = framework.getLocaleMessenger()

    /**
     * Return a list of ingoing transactions under the target online player.
     *
     * @param sender        the command sender.
     * @param player        the target online player.
     * @return              the list of ingoing transactions.
     */
    fun getIngoingTransactions(sender: CommandSender?, player: Player): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.debug(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name))
            return emptyList()
        }

        return list.stream()
            .filter { it.receiver == player.uniqueId }
            .filter { it.type == TransactionType.IN }
            .collect(Collectors.toList())
    }

    /**
     * Return a list of ingoing transactions under the target online player & selected currency.
     *
     * @param sender        the command sender.
     * @param player        the target online player.
     * @param currency      the selected currency.
     * @return              the list of ingoing transactions.
     */
    fun getIngoingTransactions(sender: CommandSender?, player: Player, currency: Currency): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.debug(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name))
            return emptyList()
        }

        return list.stream()
            .filter { it.receiver == player.uniqueId }
            .filter { it.type == TransactionType.IN && it.currency == currency.id }
            .collect(Collectors.toList())
    }

    /**
     * Return a list of ingoing transactions under the target offline player.
     *
     * @param sender        the command sender.
     * @param player        the target offline player.
     * @return              the list of ingoing transactions.
     */
    fun getIngoingTransactions(sender: CommandSender?, player: OfflinePlayer): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.debug(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name ?: "no-name"))
            return emptyList()
        }

        return list.stream()
            .filter { it.receiver == player.uniqueId }
            .filter { it.type == TransactionType.IN }
            .collect(Collectors.toList())
    }

    /**
     * Return a list of ingoing transactions under the target offline player & selected currency.
     *
     * @param sender        the command sender.
     * @param player        the target offline player.
     * @param currency      the selected currency.
     *
     * @return              the list of ingoing transactions.
     */
    fun getIngoingTransactions(sender: CommandSender?, player: OfflinePlayer, currency: Currency): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.debug(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name ?: "no-name"))
            return emptyList()
        }

        return list.stream()
            .filter { it.receiver == player.uniqueId }
            .filter { it.type == TransactionType.IN && it.currency == currency.id }
            .collect(Collectors.toList())
    }

    /**
     * Return a list of outgoing under the target online player.
     *
     * @param sender        the command sender.
     * @param player        the target online player.
     *
     * @return              list of outgoing transactions.
     */
    fun getOutgoingTransactions(sender: CommandSender?, player: Player): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.debug(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId }
            .filter { it.type == TransactionType.OUT }
            .collect(Collectors.toList())
    }

    /**
     * Return a list of outgoing under the target online player & currency selected.
     *
     * @param sender        the command sender.
     * @param player        the target online player.
     * @param currency      the selected currency.
     *
     * @return              the list of outgoing transactions.
     */
    fun getOutgoingTransactions(sender: CommandSender?, player: Player, currency: Currency): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.debug(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId }
            .filter { it.type == TransactionType.OUT && it.currency == currency.id }
            .collect(Collectors.toList())
    }

    /**
     * Return a list of outgoing under the target offline player.
     *
     * @param sender        the command sender.
     * @param player        the target offline player.
     *
     * @return              the list of outgoing transactions.
     */
    fun getOutgoingTransactions(sender: CommandSender?, player: OfflinePlayer): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.debug(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name ?: "no-name"))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId }
            .filter { it.type == TransactionType.OUT }
            .collect(Collectors.toList())
    }

    /**
     * Return a list of outgoing under the target offline player & currency selected.
     *
     * @param sender        the command sender.
     * @param player        the target offline player.
     * @param currency      the currency selected.
     *
     * @return              the list of outgoing transactions.
     */
    fun getOutgoingTransactions(sender: CommandSender?, player: OfflinePlayer, currency: Currency): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.debug(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name ?: "no-name"))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId }
            .filter { it.type == TransactionType.OUT && it.currency == currency.id }
            .collect(Collectors.toList())
    }

    /**
     * Return list of all transactions under the target online player.
     *
     * @param sender        the command sender.
     * @param player        the target online player.
     *
     * @return              the list of transactions.
     */
    fun getAllTransactions(sender: CommandSender?, player: Player): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.debug(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId || it.receiver == player.uniqueId }
            .collect(Collectors.toList())
    }

    /**
     * Return list of all transactions under the target online player & selected currency.
     *
     * @param sender        the command sender.
     * @param player        the target online player.
     * @param currency      the selected currency.
     *
     * @return              the list of transactions.
     */
    fun getAllTransactions(sender: CommandSender?, player: Player, currency: Currency): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.debug(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId || it.receiver == player.uniqueId }
            .filter { it.currency == currency.id }
            .collect(Collectors.toList())
    }

    /**
     * Return list of all transactions under the target offline player.
     *
     * @param sender        the command sender.
     * @param player        the target offline player.
     *
     * @return              the list of transactions.
     */
    fun getAllTransactions(sender: CommandSender?, player: OfflinePlayer): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.debug(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name ?: "no-name"))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId || it.receiver == player.uniqueId }
            .collect(Collectors.toList())
    }

    /**
     * Return list of all transactions under the target offline player & selected currency.
     *
     * @param sender        the command sender.
     * @param player        the target offline player.
     * @param currency      the selected currency.
     *
     * @return              the list of transactions.
     */
    fun getAllTransactions(sender: CommandSender?, player: OfflinePlayer, currency: Currency): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.debug(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name ?: "no-name"))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId || it.receiver == player.uniqueId }
            .filter { it.currency == currency.id }
            .collect(Collectors.toList())
    }

    /**
     * Convert all transactions of an online player and return a Stack based data structure.
     *
     * @param sender        the command sender.
     * @param player        the target online player of the transactions.
     * @return              the stack of indexed transactions.
     */
    fun transactionsToStack(sender: CommandSender?, player: Player): Stack<TransactionNode> {
        val stack: Stack<TransactionNode> = Stack()
        var index = 1
        getAllTransactions(sender, player)
            .sortedWith(Comparator.comparing(Transaction::created))
            .forEach {
                stack.push(TransactionNode(index, it))
                index++
            }
        return stack
    }

    /**
     * Convert all transactions of an offline player and return a Stack based data structure.
     *
     * @param sender        the command sender.
     * @param player        the target offline player of the transactions.
     * @return              the stack of indexed transactions.
     */
    fun transactionsToStack(sender: CommandSender?, player: OfflinePlayer): Stack<TransactionNode> {
        val stack: Stack<TransactionNode> = Stack()
        var index = 1
        getAllTransactions(sender, player)
            .sortedWith(Comparator.comparing(Transaction::created))
            .forEach {
                stack.push(TransactionNode(index, it))
                index++
            }
        return stack
    }
}