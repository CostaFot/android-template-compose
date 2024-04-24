package com.feelsokman.androidtemplate.di

import android.app.Application
import com.feelsokman.androidtemplate.TemplateApplication
import com.feelsokman.androidtemplate.ui.activity.MainActivity
import com.feelsokman.common.FlagProvider
import com.feelsokman.common.NetworkMonitor
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        ViewModelModule::class,
        MainActivityModule::class
    ]
)
interface AppComponent {

    fun inject(application: TemplateApplication)
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    companion object {
        lateinit var instance: AppComponent

        fun initAppComponent(
            application: Application,
        ): AppComponent {

            instance = DaggerAppComponent.builder()
                .application(application)
                .build()

            return instance
        }
    }


    fun flagProvider(): FlagProvider
    fun networkMonitor(): NetworkMonitor
}