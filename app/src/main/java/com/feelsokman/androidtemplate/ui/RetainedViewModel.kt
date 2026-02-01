package com.feelsokman.androidtemplate.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.RetainObserver
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class RetainedViewModel : RetainObserver {
    private var _viewModelScope: CoroutineScope? = null

    val viewModelScope: CoroutineScope
        get() =
            _viewModelScope ?: CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).also {
                _viewModelScope = it
            }

    override fun onEnteredComposition() {
        Log.d("COSTA", "onEnteredComposition")
    }

    override fun onExitedComposition() {
        Log.d("COSTA", "onExitedComposition")
    }

    override fun onRetained() {
        Log.d("COSTA", "onRetained")
    }

    override fun onUnused() {
        Log.d("COSTA", "onUnused")
    }

    override fun onRetired() {
        Log.d("COSTA", "onRetired")
        clear()
    }

    private fun clear() {
        _viewModelScope?.cancel()
        _viewModelScope = null

        onCleared()
    }

    protected open fun onCleared() {
        // implement for cleanup in subclass if necessary
    }
}

interface RetainedViewModelEntryPoint<T : RetainedViewModel> {
    fun create(): T
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RetainedEntryPoint(
    val value: KClass<out Any>,
)

@EntryPoint
@InstallIn(ActivityComponent::class)
interface SampleEntryPoint : RetainedViewModelEntryPoint<SampleRetainedViewModel>

@Composable
inline fun <reified T : RetainedViewModel> rememberRetainedViewModel(): T {
    val context = LocalContext.current
    return retain {
        val annotation =
            T::class.java.getAnnotation(RetainedEntryPoint::class.java)
                ?: error("${T::class} must be annotated with @RetainedEntryPoint")

        val entryPointClass = annotation.value.java
        val entryPoint = EntryPoints.get(context, entryPointClass) as RetainedViewModelEntryPoint<T>
        entryPoint.create()
    }
}

@RetainedEntryPoint(SampleEntryPoint::class)
class SampleRetainedViewModel @Inject constructor(

) : RetainedViewModel() {
    private val _state = MutableStateFlow(false)
    val state: StateFlow<Boolean> = _state.asStateFlow()

    init {
        Log.d("COSTA", "init SampleRetainedViewModel ${hashCode()}")
    }

    override fun onCleared() {
        Log.d("COSTA", "onRetired SampleRetainedViewModel ${hashCode()}")
    }
}