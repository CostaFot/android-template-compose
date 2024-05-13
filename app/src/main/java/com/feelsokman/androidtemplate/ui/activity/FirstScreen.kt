package com.feelsokman.androidtemplate.ui.activity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.feelsokman.androidtemplate.di.ViewModelFactory
import com.feelsokman.androidtemplate.di.ViewModelKey
import com.feelsokman.androidtemplate.rememberscope.rememberScoped
import com.feelsokman.logging.logDebug
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FirstScope


@Module
object FirstModule {

    @Provides
    fun providesFirstDependency(): FirstScreenTracker {
        return FirstScreenTracker()
    }


}

@Module
abstract class VmModule {
    @Binds
    @IntoMap
    @ViewModelKey(FirstViewModel::class)
    abstract fun bindsFirstViewModel(viewModel: FirstViewModel): ViewModel
}

@FirstScope
@Component(
    modules = [FirstModule::class, VmModule::class]
)
interface FirstComponent {
    fun inject(firstContainer: FirstContainer)
    @Component.Builder
    interface Builder {
        fun build(): FirstComponent
    }
}

@Stable
class FirstContainer {
    @Inject
    lateinit var firstScreenTracker: FirstScreenTracker
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
}

@Composable
fun FirstScreen(
    navigate: () -> Unit,
    firstContainer: FirstContainer = rememberFirstContainer(),
    firstViewModel: FirstViewModel = viewModel(factory = firstContainer.viewModelFactory)
) {


    val uiState by firstViewModel.uiState.collectAsStateWithLifecycle()

    InnerFirstScreen(
        firstContainer = firstContainer,
        uiState = uiState,
        navigate = navigate
    )

    Testy(firstContainer = firstContainer)
}

@Composable
private fun InnerFirstScreen(
    firstContainer: FirstContainer,
    uiState: String,
    navigate: () -> Unit,
) {
    Column {
        Spacer(modifier = Modifier.height(100.dp))
        Button(
            onClick = {
                navigate.invoke()
            }
        ) {
            Text(text = uiState)
        }
    }
}

// this is stable
@Composable
fun Testy(
    firstContainer: FirstContainer
) {

    val rr = rememberScoped {
        logDebug { "fffffffffffffffffffffffffffffffffffffffffffffffffff" }
        5
    }


    Text(text = "fefwfewfwf")

}

@Composable
fun rememberFirstContainer(): FirstContainer {
    return remember {
        FirstContainer().also {
            DaggerFirstComponent.builder().build().inject(it)
        }
    }
}

class FirstViewModel @Inject constructor(
    private val firstScreenTracker: FirstScreenTracker,
    private val secondDependency: SecondDependency
) : ViewModel() {


    val uiState = MutableStateFlow(UUID.randomUUID().toString())

    init {
        logDebug { "FirstViewModel init ${this.hashCode()}" }

        viewModelScope.launch {
            while (true) {
                delay(2000)
                uiState.update {
                    UUID.randomUUID().toString()
                }
            }
        }
    }

    override fun onCleared() {
        logDebug { "FirstViewModel onCleared" }
        super.onCleared()
    }
}