package com.feelsokman.androidtemplate.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.feelsokman.androidtemplate.ui.activity.SecondViewModel
import com.feelsokman.androidtemplate.ui.activity.ThirdViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SecondViewModel::class)
    abstract fun bindsSecondViewModel(viewModel: SecondViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ThirdViewModel::class)
    abstract fun bindsThirdViewModel(viewModel: ThirdViewModel): ViewModel

}
