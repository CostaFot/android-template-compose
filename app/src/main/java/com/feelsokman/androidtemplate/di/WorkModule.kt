package com.feelsokman.androidtemplate.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.feelsokman.androidtemplate.core.features.FlagProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkModule {

    @Provides
    @Singleton
    internal fun providesWorkManager(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)

    @Singleton
    @Provides
    fun provideWorkManagerConfiguration(
        flagProvider: FlagProvider,
        workerFactory: HiltWorkerFactory
    ): Configuration {
        return Configuration.Builder().apply {
            if (flagProvider.isDebugEnabled) {
                setMinimumLoggingLevel(android.util.Log.DEBUG)
            }
            setWorkerFactory(workerFactory)
        }.build()
    }
}
