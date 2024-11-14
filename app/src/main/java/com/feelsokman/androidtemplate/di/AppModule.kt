package com.feelsokman.androidtemplate.di

import android.content.Context
import android.content.res.Resources
import com.feelsokman.androidtemplate.BuildConfig
import com.feelsokman.androidtemplate.ui.activity.MainActivity
import com.feelsokman.common.di.DebugFlag
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun providesApplicationResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Singleton
    @Provides
    @DebugFlag
    fun providesDebugFlag(): Boolean = BuildConfig.DEBUG

    @Provides
    fun balls(): Balls {
        return Balls()
    }
}


@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        AppModule::class
    ]
)
interface SingletonModule


@InstallIn(SingletonComponent::class)
@EntryPoint
interface AppComponent {
    fun balls(): Balls
}

@Component(dependencies = [AppComponent::class])
interface SomeComponent {

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): SomeComponent
    }

    fun inject(mainActivity: MainActivity)
}