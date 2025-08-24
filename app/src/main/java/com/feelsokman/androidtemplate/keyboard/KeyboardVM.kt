package com.feelsokman.androidtemplate.keyboard

import android.app.Application
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.navEntryDecorator
import com.feelsokman.androidtemplate.domain.JsonPlaceHolderRepository
import com.feelsokman.androidtemplate.keyboard.second.SecondScreenVM
import com.feelsokman.androidtemplate.keyboard.start.StartScreenVM
import com.feelsokman.common.coroutine.DispatcherProvider
import com.feelsokman.logging.logDebug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.reflect.KClass

class KeyboardVM @Inject constructor(
    private val jsonPlaceHolderRepository: JsonPlaceHolderRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val application: Application,
) : CustomViewModelOwner {

    private val _textData = MutableStateFlow(UUID.randomUUID().toString())
    val uiState: StateFlow<String>
        get() = _textData

    private val viewModelScope by lazy {
        CoroutineScope(dispatcherProvider.ui + SupervisorJob())
    }

    val backStack = mutableStateListOf<Any>(RouteA)
    val customViewModelStore = mutableMapOf<Any, CustomViewModel>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : CustomViewModel> get(
        key: Any,
        modelClass: KClass<T>
    ): T {

        return customViewModelStore.getOrPut(key) {
            val entryPoint = application.keyboardEntryPoint()
            when (modelClass) {
                StartScreenVM::class -> {
                    entryPoint.startScreenVM()


                }

                SecondScreenVM::class -> {
                    entryPoint.secondScreenVM()
                }

                else -> throw IllegalStateException("Unknown class $modelClass")
            }
        } as T
    }


    val onPop: (Any) -> Unit = { key ->
        logDebug { "Popping $key" }
        logDebug { "${customViewModelStore.keys}" }
        customViewModelStore[key]?.let { vm ->
            vm.onCleared()
            customViewModelStore.remove(key)
            logDebug { "Cleared VM for $key" }
        }
    }

    val customDecorator: NavEntryDecorator<Any> = navEntryDecorator(onPop) { entry ->
        logDebug {
            "CustomDecorator called for with key: ${entry.contentKey}"
        }

        CompositionLocalProvider(
            LocalKeyProvider provides entry.contentKey
        ) {
            entry.Content()
        }


    }


    init {
        getTodo()
        logDebug { "Init KeyboardVM, id: ${hashCode()}" }
    }

    fun getTodo() {
        viewModelScope.launch {
            delay(5000)
            _textData.update { UUID.randomUUID().toString() }
            /*  delay(5000)
              jsonPlaceHolderRepository.getTodo(2).fold(
                  ifError = {
                      logError { it.toString() }
                  },
                  ifSuccess = {
                      logDebug { it.title }
                      _textData.update { UUID.randomUUID().toString() }
                  }
              )*/

        }
    }

    fun onCleared() {
        logDebug { "OnDestroy KeyboardVM" }
        viewModelScope.coroutineContext.cancelChildren()
    }


}
