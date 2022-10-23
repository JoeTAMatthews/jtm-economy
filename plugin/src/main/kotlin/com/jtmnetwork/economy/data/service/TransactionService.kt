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

    fun getOutgoingTransactions(sender: CommandSender?, player: Player): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.warn(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId }
            .filter { it.type == TransactionType.OUT }
            .collect(Collectors.toList())
    }

    fun getOutgoingTransactions(sender: CommandSender?, player: Player, currency: Currency): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.warn(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId }
            .filter { it.type == TransactionType.OUT && it.currency == currency.id }
            .collect(Collectors.toList())
    }

    fun getOutgoingTransactions(sender: CommandSender?, player: OfflinePlayer): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.warn(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name ?: "no-name"))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId }
            .filter { it.type == TransactionType.OUT }
            .collect(Collectors.toList())
    }

    fun getOutgoingTransactions(sender: CommandSender?, player: OfflinePlayer, currency: Currency): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.warn(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name ?: "no-name"))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId }
            .filter { it.type == TransactionType.OUT && it.currency == currency.id }
            .collect(Collectors.toList())
    }

    fun getIngoingTransactions(sender: CommandSender?, player: Player): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.warn(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name))
            return emptyList()
        }

        return list.stream()
            .filter { it.receiver == player.uniqueId }
            .filter { it.type == TransactionType.IN }
            .collect(Collectors.toList())
    }

    fun getIngoingTransactions(sender: CommandSender?, player: Player, currency: Currency): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.warn(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name))
            return emptyList()
        }

        return list.stream()
            .filter { it.receiver == player.uniqueId }
            .filter { it.type == TransactionType.IN && it.currency == currency.id }
            .collect(Collectors.toList())
    }

    fun getIngoingTransactions(sender: CommandSender?, player: OfflinePlayer): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.warn(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name ?: "no-name"))
            return emptyList()
        }

        return list.stream()
            .filter { it.receiver == player.uniqueId }
            .filter { it.type == TransactionType.IN }
            .collect(Collectors.toList())
    }

    fun getIngoingTransactions(sender: CommandSender?, player: OfflinePlayer, currency: Currency): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.warn(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name ?: "no-name"))
            return emptyList()
        }

        return list.stream()
            .filter { it.receiver == player.uniqueId }
            .filter { it.type == TransactionType.IN && it.currency == currency.id }
            .collect(Collectors.toList())
    }

    fun getAllTransactions(sender: CommandSender?, player: Player): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.warn(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId || it.receiver == player.uniqueId }
            .collect(Collectors.toList())
    }

    fun getAllTransactions(sender: CommandSender?, player: Player, currency: Currency): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.warn(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId || it.receiver == player.uniqueId }
            .filter { it.currency == currency.id }
            .collect(Collectors.toList())
    }

    fun getAllTransactions(sender: CommandSender?, player: OfflinePlayer): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.warn(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name ?: "no-name"))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId || it.receiver == player.uniqueId }
            .collect(Collectors.toList())
    }

    fun getAllTransactions(sender: CommandSender?, player: OfflinePlayer, currency: Currency): List<Transaction> {
        val list = getAll()
        if (list.isEmpty()) {
            if (sender != null) messenger.sendMessage(sender, "transactions.no_found")
            logging.warn(format("%s(%s) has no transactions.", player.uniqueId.toString(), player.name ?: "no-name"))
            return emptyList()
        }

        return list.stream()
            .filter { it.sender == player.uniqueId || it.receiver == player.uniqueId }
            .filter { it.currency == currency.id }
            .collect(Collectors.toList())
    }

    /**
     * Retrieve all transactions based on the player being a receiver.
     *
     * @param id            the target identifier
     * @return              the list of transactions.
     */
    fun getByReceiver(id: UUID): List<Transaction> {
        return getAll()
                .stream()
                .filter { it.receiver == id && it.type == TransactionType.IN }
                .collect(Collectors.toList()) ?: listOf()
    }

    /**
     * Retrieve all transactions based on the player being a sender.
     *
     * @param id            the target identifier.
     * @return              the list of transactions.
     */
    fun getBySender(id: UUID): List<Transaction> {
        return getAll()
                .stream()
                .filter { it.sender == id && it.type == TransactionType.OUT }
                .collect(Collectors.toList()) ?: listOf()
    }

    /**
     * Retrieve all transactions based on the player being a receiver and sender.
     *
     * @param id            the target identifier
     * @return              the list of transactions.
     */
    fun getByReceiverAndSender(id: UUID): List<Transaction> {
        return getAll()
                .stream()
                .filter { it.receiver == id && it.type == TransactionType.IN || it.sender == id && it.type == TransactionType.OUT }
                .collect(Collectors.toList()) ?: listOf()
    }

    /**
     * Retrieve all transactions based on the currency.
     *
     * @param id            the target identifier
     * @param currency      the currency identifier
     * @return              the list of transactions.
     */
    fun getByCurrency(id: UUID, currency: UUID): List<Transaction> {
        return getAll()
                .stream()
                .filter { (it.receiver == id && it.type == TransactionType.IN || it.sender == id && it.type == TransactionType.OUT) && it.currency == currency }
                .collect(Collectors.toList()) ?: listOf()
    }

    /**
     * Convert all transactions and return a Stack based data structure.
     *
     * @param receiver      the identifier of the target
     * @return              the stack of indexed transactions.
     */
    fun transactionsToStack(receiver: UUID): Stack<TransactionNode> {
        val stack: Stack<TransactionNode> = Stack()
        var index = 1
        getByReceiverAndSender(receiver)
                .sortedWith(Comparator.comparing(Transaction::created))
                .forEach {
                    stack.push(TransactionNode(index, it))
                    index++
                }
        return stack
    }
}