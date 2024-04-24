package com.feelsokman.androidtemplate.ui.activity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.feelsokman.androidtemplate.di.AppComponent
import com.feelsokman.androidtemplate.di.ViewModelFactory
import com.feelsokman.androidtemplate.di.ViewModelKey
import com.feelsokman.common.FlagProvider
import com.feelsokman.logging.logDebug
import com.sebaslogen.resaca.rememberScoped
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
    fun providesFirstDependency(flagProvider: FlagProvider): FirstDependency {
        return FirstDependency(flagProvider)
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
    dependencies = [AppComponent::class],
    modules = [FirstModule::class, VmModule::class]
)
interface FirstComponent {

    fun inject(firstContainer: FirstContainer)

    @Component.Builder
    interface Builder {

        fun container(appComponent: AppComponent): Builder

        fun build(): FirstComponent
    }
}

@Stable
class FirstContainer : ViewModel() {

    @Inject
    lateinit var firstDependency: FirstDependency

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    fun isEnabled() = firstDependency.doSomething()

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
                firstContainer.isEnabled()
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

    Text(text = "fefwfewfwf")

}

@Composable
fun rememberFirstContainer(): FirstContainer {
    val appComponentProvider = LocalAppComponentProvider.current
    return rememberScoped(appComponentProvider) {
        logDebug { "gggggggggggggggggggggggggggggggggggggggggggggg" }
        // build component and container with all screen deps
        FirstContainer().also {
            DaggerFirstComponent.builder().container(appComponentProvider.get.invoke()).build().inject(it)
        }
    }
}

class FirstViewModel @Inject constructor(
    private val firstDependency: FirstDependency,
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