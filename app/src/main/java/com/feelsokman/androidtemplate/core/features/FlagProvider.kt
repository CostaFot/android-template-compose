package com.feelsokman.androidtemplate.core.features

import com.feelsokman.androidtemplate.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlagProvider @Inject constructor() {
    val isDebugEnabled = BuildConfig.DEBUG
}
