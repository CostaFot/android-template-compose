package com.feelsokman.common.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import javax.inject.Qualifier
import kotlin.reflect.KClass

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationCoroutineScope


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DebugFlag

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RunningUiTestFlag


@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
