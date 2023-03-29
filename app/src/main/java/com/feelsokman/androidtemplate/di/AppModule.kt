package com.feelsokman.androidtemplate.di

import android.content.Context
import android.content.res.Resources
import com.feelsokman.androidtemplate.core.coroutine.DefaultApplicationCoroutineScope
import com.feelsokman.androidtemplate.core.coroutine.DefaultDispatcherProvider
import com.feelsokman.androidtemplate.core.coroutine.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun providesApplicationResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }
}
