package com.feelsokman.androidtemplate.retain

import androidx.compose.runtime.retain.RetainedValuesStore

class RetainedValuesStoreImpl : RetainedValuesStore {
    override fun consumeExitedValueOrDefault(
        key: Any,
        defaultValue: Any?
    ): Any? {
        TODO("Not yet implemented")
    }

    override fun saveExitingValue(key: Any, value: Any?) {
        TODO("Not yet implemented")
    }

    override fun onContentEnteredComposition() {
        TODO("Not yet implemented")
    }

    override fun onContentExitComposition() {
        TODO("Not yet implemented")
    }

}