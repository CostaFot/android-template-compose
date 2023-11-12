package com.feelsokman.auth

import android.accounts.Account
import android.accounts.AccountManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidAccountManager @Inject constructor(
    private val accountManager: AccountManager
) {
    val userState: StateFlow<User>
        get() = _userState

    private val _userState by lazy {
        MutableStateFlow(getUser())
    }

    sealed class User {
        data object LoggedOut : User()
        data class LoggedIn(
            val name: String,
            val email: String
        ) : User()
    }

    fun addAccount(username: String, email: String): Boolean = update {
        accountManager.addAccountExplicitly(
            Account(username, ACCOUNT_TYPE),
            null,
            null
        )

        updateAccount(username, email)
    }

    fun removeAccount(): Boolean = update {
        getAccount()?.let {
            accountManager.removeAccountExplicitly(it)
        } ?: run {
            false
        }
    }

    fun updateAccount(name: String, email: String): Boolean = update {
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

    private fun getUser(): User {
        return getAccount()?.let {
            User.LoggedIn(
                accountManager.getUserData(it, KEY_NAME).orEmpty(),
                accountManager.getUserData(it, KEY_EMAIL).orEmpty()
            )
        } ?: run {
            User.LoggedOut
        }
    }

    private fun <T> update(block: () -> T): T {
        return block().also {
            _userState.update { getUser() }
        }
    }


    private fun getAccount(): Account? = accountManager.getAccountsByType(ACCOUNT_TYPE).firstOrNull()

    companion object {
        private const val ACCOUNT_TYPE = "com.example.account.type"
        private const val KEY_NAME = "KEY_NAME"
        private const val KEY_EMAIL = "KEY_EMAIL"
    }
}
