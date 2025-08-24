package com.feelsokman.androidtemplate.keyboard

import android.app.Application
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.navEntryDecorator
import com.feelsokman.androidtemplate.keyboard.second.SecondScreenVM
import com.feelsokman.androidtemplate.keyboard.start.StartScreenVM
import com.feelsokman.logging.logDebug
import javax.inject.Inject
import kotlin.reflect.KClass

class KeyboardViewModel @Inject constructor(
    private val application: Application,
) : CustomViewModelOwner {
    val backStack = mutableStateListOf<Any>(RouteA)
    val customViewModelStore = mutableMapOf<Any, CustomViewModel>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : CustomViewModel> get(
        key: Any,
        modelClass: KClass<T>
    ): T = customViewModelStore.getOrPut(key) {
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
        logDebug { "Init KeyboardVM, id: ${hashCode()}" }
    }
}
