package com.jtmnetwork.economy.entrypoint.vault

import com.jtmnetwork.economy.Economy
import com.jtmnetwork.economy.entrypoint.api.EconomyAPI
import net.milkbowl.vault.economy.AbstractEconomy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit

class VaultEconomy(private val economy: Economy, private val economyAPI: EconomyAPI): AbstractEconomy() {

    override fun isEnabled(): Boolean {
        return economy.isVaultEnabled()
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
        return ""
    }

    override fun currencyNamePlural(): String {
        val global = economyAPI.getGlobalCurrency() ?: return ""
        return global.abbreviation
    }

    override fun currencyNameSingular(): String {
        val global = economyAPI.getGlobalCurrency() ?: return ""
        return global.abbreviation
    }

    override fun hasAccount(playerName: String): Boolean {
        val player = Bukkit.getPlayer(playerName) ?: return false
        economyAPI.getWallet(player) ?: return false
        return true
    }

    override fun hasAccount(playerName: String, worldName: String?): Boolean {
        val player = Bukkit.getPlayer(playerName) ?: return false
        economyAPI.getWallet(player) ?: return false
        return true
    }

    override fun getBalance(playerName: String): Double {
        val player = Bukkit.getPlayer(playerName) ?: return 0.0
        val wallet = economyAPI.getWallet(player) ?: return 0.0
        val currency = economyAPI.getGlobalCurrency() ?: return 0.0
        return wallet.getBalance(currency.id)
    }

    override fun getBalance(playerName: String, world: String?): Double {
        val player = Bukkit.getPlayer(playerName) ?: return 0.0
        val wallet = economyAPI.getWallet(player) ?: return 0.0
        val currency = economyAPI.getGlobalCurrency() ?: return 0.0
        return wallet.getBalance(currency.id)
    }

    override fun has(playerName: String?, amount: Double): Boolean {
        return false
    }

    override fun has(playerName: String?, worldName: String?, amount: Double): Boolean {
        return false
    }

    override fun withdrawPlayer(playerName: String, amount: Double): EconomyResponse {
        val player = Bukkit.getPlayer(playerName) ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find player.")
        val currency = economyAPI.getGlobalCurrency() ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find global currency.")
        val trans = economyAPI.withdraw(player, currency.id, null, amount) ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Failed to withdraw.")
        return EconomyResponse(amount, trans.new_balance, EconomyResponse.ResponseType.SUCCESS, "")
    }

    override fun withdrawPlayer(playerName: String, worldName: String?, amount: Double): EconomyResponse {
        val player = Bukkit.getPlayer(playerName) ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find player.")
        val currency = economyAPI.getGlobalCurrency() ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find global currency.")
        val trans = economyAPI.withdraw(player, currency.id, null, amount) ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Failed to withdraw.")
        return EconomyResponse(amount, trans.new_balance, EconomyResponse.ResponseType.SUCCESS, "")
    }

    override fun depositPlayer(playerName: String, amount: Double): EconomyResponse {
        val player = Bukkit.getPlayer(playerName) ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find player.")
        val currency = economyAPI.getGlobalCurrency() ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find global currency.")
        val trans = economyAPI.deposit(player, currency.id, null, amount) ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Failed to deposit.")
        return EconomyResponse(amount, trans.new_balance, EconomyResponse.ResponseType.SUCCESS, "")
    }

    override fun depositPlayer(playerName: String, worldName: String?, amount: Double): EconomyResponse {
        val player = Bukkit.getPlayer(playerName) ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find player.")
        val currency = economyAPI.getGlobalCurrency() ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Can't find global currency.")
        val trans = economyAPI.deposit(player, currency.id, null, amount) ?: return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Failed to withdraw.")
        return EconomyResponse(amount, trans.new_balance, EconomyResponse.ResponseType.SUCCESS, "")
    }

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
        return economyAPI.getCurrencies().map { it.name }.toMutableList()
    }

    override fun createPlayerAccount(playerName: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun createPlayerAccount(playerName: String?, worldName: String?): Boolean {
        TODO("Not yet implemented")
    }
}