package com.feelsokman.androidtemplate.keyboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import kotlin.reflect.KClass

object LocalCustomViewModelOwner {
    private val LocalLocalCustomViewModelOwner =
        compositionLocalOf<CustomViewModelOwner?> { null }

    val current: CustomViewModelOwner?
        @Composable
        get() =
            LocalLocalCustomViewModelOwner.current!!

    infix fun provides(
        customViewModelOwner: CustomViewModelOwner
    ): ProvidedValue<CustomViewModelOwner?> {
        return LocalLocalCustomViewModelOwner.provides(customViewModelOwner)
    }
}


interface CustomViewModelOwner {

    fun <T : CustomViewModel> get(key: Any, modelClass: KClass<T>): T

}


inline fun <reified T : CustomViewModel> CustomViewModelOwner.get(key: Any): T = get(key, T::class)