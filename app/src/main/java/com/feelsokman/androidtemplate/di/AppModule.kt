package com.feelsokman.androidtemplate.di

import android.app.Application
import com.feelsokman.androidtemplate.BuildConfig
import com.feelsokman.common.AppLocaleManager
import com.feelsokman.common.ConnectivityManagerNetworkMonitor
import com.feelsokman.common.FlagProvider
import com.feelsokman.common.NetworkMonitor
import com.feelsokman.common.ProdLocaleManager
import com.feelsokman.common.coroutine.DefaultApplicationCoroutineScope
import com.feelsokman.common.coroutine.DefaultDispatcherProvider
import com.feelsokman.common.coroutine.DispatcherProvider
import com.feelsokman.common.di.ApplicationCoroutineScope
import com.feelsokman.common.di.DebugFlag
import com.feelsokman.common.di.RunningUiTestFlag
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    @DebugFlag
    fun providesDebugFlag(): Boolean = BuildConfig.DEBUG


    @Singleton
    @Provides
    fun providesNetworkMonitor(
        application: Application
    ): NetworkMonitor = ConnectivityManagerNetworkMonitor(application.applicationContext)

    @Singleton
    @Provides
    fun providesFlagProvider(
        @DebugFlag debugFlag: Boolean,
        @RunningUiTestFlag uiTestFlag: Boolean
    ): FlagProvider = object : FlagProvider {
        override val isDebugEnabled: Boolean
            get() = debugFlag
        override val isRunningUiTest: Boolean
            get() = uiTestFlag
    }

    @Singleton
    @Provides
    fun providesAppLocaleManager(
        application: Application
    ): AppLocaleManager = ProdLocaleManager(application)

    @Singleton
    @Provides
    @RunningUiTestFlag
    fun providesRunningUiTest(): Boolean = false

    @Singleton
    @Provides
    @ApplicationCoroutineScope
    fun providesApplicationScope(): CoroutineScope = DefaultApplicationCoroutineScope

    @Singleton
    @Provides
    fun providesDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider


}
