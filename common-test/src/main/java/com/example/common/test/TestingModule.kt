package com.example.common.test

import com.feelsokman.common.di.ProductionModule
import com.feelsokman.common.di.RunningUiTestFlag
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ProductionModule::class],
)
object TestingModule {
    @Singleton
    @Provides
    @RunningUiTestFlag
    fun providesRunningUiTest(): Boolean = true
}
