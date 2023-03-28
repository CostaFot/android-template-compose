package com.feelsokman.androidtemplate.di

import com.feelsokman.androidtemplate.core.coroutine.DefaultApplicationCoroutineScope
import com.feelsokman.androidtemplate.core.coroutine.DefaultDispatcherProvider
import com.feelsokman.androidtemplate.core.coroutine.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @ApplicationCoroutineScope
    fun providesApplicationScope(): CoroutineScope = DefaultApplicationCoroutineScope

    @Singleton
    @Provides
    fun providesDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider
}
