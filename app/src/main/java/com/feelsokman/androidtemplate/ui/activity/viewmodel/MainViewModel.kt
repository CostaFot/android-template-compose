package com.feelsokman.androidtemplate.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feelsokman.auth.AndroidAccountManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val androidAccountManager: AndroidAccountManager
) : ViewModel() {

    val uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)

    sealed class UiState {

        data object Loading : UiState()

        data class LoggedOut(
            val options: List<Option>
        ) : UiState()

        data class LoggedIn(
            val name: String,
            val email: String,
            val options: List<Option>
        ) : UiState()

        sealed class Option {
            data object LogIn : Option()
            data object LogOut : Option()
            data object UpdateAccount : Option()
        }

    }

    init {
        viewModelScope.launch {
            androidAccountManager.userState.collect { user ->
                uiState.value = buildUiState(user)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            androidAccountManager.removeAccount()
        }
    }

    fun updateAccount() {
        viewModelScope.launch {
            val result = androidAccountManager.updateAccount(
                "Detective Mittens",
                "detectivemittens@example.com"
            )
        }
    }

    fun login() {
        viewModelScope.launch {
            androidAccountManager.addAccount("mittens", "mittens@example.com")
        }
    }

    private fun buildUiState(user: AndroidAccountManager.User): UiState {
        return when (user) {
            is AndroidAccountManager.User.LoggedIn -> {
                UiState.LoggedIn(
                    name = user.name,
                    email = user.email,
                    options = listOf(
                        UiState.Option.LogOut,
                        UiState.Option.UpdateAccount
                    )
                )
            }

            AndroidAccountManager.User.LoggedOut -> {
                UiState.LoggedOut(
                    options = listOf(
                        UiState.Option.LogIn
                    )
                )
            }
        }
    }



}
