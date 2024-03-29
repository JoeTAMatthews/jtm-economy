package com.jtmnetwork.economy.entrypoint.vault

import com.jtmnetwork.economy.JtmEconomy
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import com.jtmnetwork.economy.entrypoint.api.WalletAPI
import net.milkbowl.vault.economy.AbstractEconomy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit

open class VaultEconomy(private val jtmEconomy: JtmEconomy): AbstractEconomy() {

    private val exchangeAPI = jtmEconomy.getExchangeAPI()
    private val walletAPI = jtmEconomy.getWalletAPI()
    private val currencyAPI = jtmEconomy.getCurrencyAPI()
    private val transactionAPI = jtmEconomy.getTransactionAPI()

    override fun isEnabled(): Boolean {
        return jtmEconomy.isVaultEnabled()
    }

    override fun getName(): String {
        return "JTMEconomy"
    }

    override fun hasBankSupport(): Boolean {
        return true
    }

    override fun fractionalDigits(): Int {
        return 0
    }

    override fun format(amount: Double): String {
        val opt = currencyAPI.getPrimaryCurrency(null)
        if (opt.isEmpty) return ""
        val currency = opt.get()
        return currency.getSymbolAmount(amount)
    }

    override fun currencyNamePlural(): String {
        val opt = currencyAPI.getPrimaryCurrency(null)
        if (opt.isEmpty) return ""
        val currency = opt.get()
        return currency.abbreviation
    }

    override fun currencyNameSingular(): String {
        val opt = currencyAPI.getPrimaryCurrency(null)
        if (opt.isEmpty) return ""
        val currency = opt.get()
        return currency.abbreviation
    }

    @Deprecated("Deprecated in Java")
    override fun hasAccount(playerName: String): Boolean {
        val player = Bukkit.getPlayer(playerName) ?: return false
        val opt = walletAPI.getWallet(null, player)
        if (opt.isEmpty) return false
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun hasAccount(playerName: String, worldName: String?): Boolean {
        val player = Bukkit.getPlayer(playerName) ?: return false
        val opt = walletAPI.getWallet(null, player)
        if (opt.isEmpty) return false
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun getBalance(playerName: String): Double {
        val player = Bukkit.getPlayer(playerName) ?: return 0.0
        val optCurrency = currencyAPI.getPrimaryCurrency(null)
        if (optCurrency.isEmpty) return 0.0
        val currency = optCurrency.get()

        return walletAPI.balance(null, player, currency).orElse(null)
    }

    @Deprecated("Deprecated in Java")
    override fun getBalance(playerName: String, world: String?): Double {
        val player = Bukkit.getPlayer(playerName) ?: return 0.0
        val optCurrency = currencyAPI.getPrimaryCurrency(null)
        if (optCurrency.isEmpty) return 0.0
        val currency = optCurrency.get()

        return walletAPI.balance(null, player, currency).orElse(null)
    }

    @Deprecated("Deprecated in Java")
    override fun has(playerName: String, amount: Double): Boolean {
        val player = Bukkit.getPlayer(playerName) ?: return false

        val optCurrency = currencyAPI.getPrimaryCurrency(null)
        if (optCurrency.isEmpty) return false
        val currency = optCurrency.get()

        return walletAPI.hasBalance(null, player, currency, amount)
    }

    @Deprecated("Deprecated in Java")
    override fun has(playerName: String, worldName: String?, amount: Double): Boolean {
        val player = Bukkit.getPlayer(playerName) ?: return false
        val optCurrency = currencyAPI.getPrimaryCurrency(null)
        if (optCurrency.isEmpty) return false
        val currency = optCurrency.get()

        return walletAPI.hasBalance(null, player, currency, amount)
    }

    @Deprecated("Deprecated in Java")
    override fun withdrawPlayer(playerName: String, amount: Double): EconomyResponse {
        val player = Bukkit.getPlayer(playerName) ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find player.")

        val opt = currencyAPI.getPrimaryCurrency(null)
        if (opt.isEmpty) return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find global currency.")
        val currency = opt.get()

        val optTrans = walletAPI.withdraw(null, player, null, currency, amount)
        if (optTrans.isEmpty) return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Insufficient funds.")
        val trans = optTrans.get()

        return EconomyResponse(amount, trans.new_balance, EconomyResponse.ResponseType.SUCCESS, "")
    }

    @Deprecated("Deprecated in Java")
    override fun withdrawPlayer(playerName: String, worldName: String?, amount: Double): EconomyResponse {
        val player = Bukkit.getPlayer(playerName) ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find player.")

        val opt = currencyAPI.getPrimaryCurrency(null)
        if (opt.isEmpty) return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find global currency.")
        val currency = opt.get()

        val optTrans = walletAPI.withdraw(null, player, null, currency, amount)
        if (optTrans.isEmpty) return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Failed to withdraw.")
        val trans = optTrans.get()

        return EconomyResponse(amount, trans.new_balance, EconomyResponse.ResponseType.SUCCESS, "")
    }

    @Deprecated("Deprecated in Java")
    override fun depositPlayer(playerName: String, amount: Double): EconomyResponse {
        val player = Bukkit.getPlayer(playerName) ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find player.")

        val opt = currencyAPI.getPrimaryCurrency(null)
        if (opt.isEmpty) return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find global currency.")
        val currency = opt.get()

        val optTrans = walletAPI.deposit(null, player, null, currency, amount)
        if (optTrans.isEmpty) return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Failed to withdraw.")
        val trans = optTrans.get()

        return EconomyResponse(amount, trans.new_balance, EconomyResponse.ResponseType.SUCCESS, "")
    }

    @Deprecated("Deprecated in Java")
    override fun depositPlayer(playerName: String, worldName: String?, amount: Double): EconomyResponse {
        val player = Bukkit.getPlayer(playerName) ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find player.")

        val opt = currencyAPI.getPrimaryCurrency(null)
        if (opt.isEmpty) return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find global currency.")
        val currency = opt.get()

        val optTrans = walletAPI.deposit(null, player, null, currency, amount)
        if (optTrans.isEmpty) return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Failed to withdraw.")
        val trans = optTrans.get()

        return EconomyResponse(amount, trans.new_balance, EconomyResponse.ResponseType.SUCCESS, "")
    }

    @Deprecated("Deprecated in Java")
    override fun createBank(name: String?, player: String?): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented.")
    }

    override fun deleteBank(name: String?): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented.")
    }

    override fun bankBalance(name: String?): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented.")
    }

    override fun bankHas(name: String?, amount: Double): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented.")
    }

    override fun bankWithdraw(name: String?, amount: Double): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented.")
    }

    override fun bankDeposit(name: String?, amount: Double): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented.")
    }

    @Deprecated("Deprecated in Java")
    override fun isBankOwner(name: String?, playerName: String?): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented.")
    }

    @Deprecated("Deprecated in Java")
    override fun isBankMember(name: String?, playerName: String?): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented.")
    }

    override fun getBanks(): MutableList<String> {
        return currencyAPI.getCurrencies().map { it.name }.toMutableList()
    }

    @Deprecated("Deprecated in Java")
    override fun createPlayerAccount(playerName: String?): Boolean {
        return false
    }

    @Deprecated("Deprecated in Java")
    override fun createPlayerAccount(playerName: String?, worldName: String?): Boolean {
        return false
    }
}