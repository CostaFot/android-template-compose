package com.feelsokman.androidtemplate

import android.app.Application
import com.feelsokman.androidtemplate.core.initialize.AppInitializer
import com.feelsokman.androidtemplate.di.AppComponent
import com.feelsokman.logging.logDebug
import javax.inject.Inject

class TemplateApplication : Application(), HasComponent<AppComponent> {

    @Inject
    lateinit var appInitializer: AppInitializer

    override fun onCreate() {
        super.onCreate()
        initialiseDagger()
        appInitializer.startup()
        logDebug { "onCreate application" }
    }

    private fun initialiseDagger() {
        AppComponent.initAppComponent(this).inject(this)
    }

    override val component: AppComponent by lazy {
        AppComponent.instance
    }

}

interface HasComponent<T> {
    val component: T
}

fun <T> Any.getComponent(): T = (this as HasComponent<T>).component
