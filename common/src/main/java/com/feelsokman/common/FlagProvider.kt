package com.feelsokman.common

import com.feelsokman.common.di.DebugFlag
import com.feelsokman.common.di.RunningUiTestFlag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlagProvider @Inject constructor(
    @DebugFlag private val debugFlag: Boolean,
    @RunningUiTestFlag private val uiTestFlag: Boolean
) {
    val isDebugEnabled = debugFlag
    val isRunningUiTest = uiTestFlag
}
