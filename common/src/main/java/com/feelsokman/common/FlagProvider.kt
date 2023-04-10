package com.feelsokman.common

import com.feelsokman.common.di.DebugFlag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlagProvider @Inject constructor(
    @DebugFlag private val debugFlag: Boolean
) {
    val isDebugEnabled = debugFlag
}
