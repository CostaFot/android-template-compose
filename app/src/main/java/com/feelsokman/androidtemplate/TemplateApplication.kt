package com.feelsokman.androidtemplate

import android.app.Application
import androidx.work.Configuration
import com.feelsokman.androidtemplate.core.initialize.AppInitializer
import com.feelsokman.logging.logDebug
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@HiltAndroidApp
class TemplateApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var appInitializer: AppInitializer

    @Inject
    lateinit var workerConfiguration: Configuration

    override fun onCreate() {
        super.onCreate()
        appInitializer.startup(this)
        logDebug { "onCreate application" }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return workerConfiguration
    }

}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ApplicationEntryPoint {
    fun appInitializer(): AppInitializer
}
