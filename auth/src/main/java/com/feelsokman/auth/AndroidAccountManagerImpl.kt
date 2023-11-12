package com.feelsokman.auth

import android.accounts.Account
import android.accounts.AccountManager
import androidx.core.os.bundleOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface AndroidAccountManager {
    val userState: StateFlow<User>
    fun addAccount(username: String, email: String): Boolean
    fun removeAccount(): Boolean
    fun updateAccount(name: String, email: String): Boolean
    sealed class User {
        data object LoggedOut : User()
        data class LoggedIn(
            val name: String,
            val email: String
        ) : User()
    }
}

class AndroidAccountManagerImpl(
    private val accountManager: AccountManager
) : AndroidAccountManager {
    override val userState: StateFlow<AndroidAccountManager.User>
        get() = _userState

    private val _userState by lazy {
        MutableStateFlow(getUser())
    }

    private val accountType = "com.example.account.type"
    override fun addAccount(username: String, email: String): Boolean = update {
        accountManager.addAccountExplicitly(
            Account(username, accountType),
            null,
            bundleOf(
                KEY_NAME to username,
                KEY_EMAIL to email
            )
        )
    }

    override fun removeAccount(): Boolean = update {
        getAccount()?.let {
            val isAccountDeleted: Boolean = accountManager.removeAccountExplicitly(it)
            isAccountDeleted
        } ?: run {
            false
        }
    }

    override fun updateAccount(name: String, email: String): Boolean = update {
        getAccount()?.let {
            updateUserData(
                it,
                mapOf(
                    KEY_NAME to name,
                    KEY_EMAIL to email
                )
            )
            true
        } ?: run {
            false
        }
    }

    private fun updateUserData(
        account: Account,
        map: Map<String, String>
    ) {
        map.forEach {
            accountManager.setUserData(account, it.key, it.value)
        }
    }

    private fun <T> update(block: () -> T): T {
        return block().also {
            _userState.update { getUser() }
        }
    }

    private fun getUser(): AndroidAccountManager.User {
        return getAccount()?.let {
            AndroidAccountManager.User.LoggedIn(
                accountManager.getUserData(it, KEY_NAME).orEmpty(),
                accountManager.getUserData(it, KEY_EMAIL).orEmpty()
            )
        } ?: run {
            AndroidAccountManager.User.LoggedOut
        }
    }

    private fun getAccount(): Account? = accountManager.getAccountsByType(accountType).firstOrNull()

    companion object {
        private const val ACCOUNT_TYPE = "com.example.account.type"
        private const val KEY_NAME = "KEY_NAME"
        private const val KEY_EMAIL = "KEY_EMAIL"
    }
}
