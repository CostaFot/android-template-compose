package com.feelsokman.androidtemplate.keyboard

import android.app.Application
import com.feelsokman.androidtemplate.keyboard.second.SecondScreenVM
import com.feelsokman.androidtemplate.keyboard.start.StartScreenVM
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface KeyboardEntryPoint {

    fun startScreenVM(): StartScreenVM
    fun secondScreenVM(): SecondScreenVM
}

fun Application.keyboardEntryPoint() =
    EntryPointAccessors.fromApplication(this, KeyboardEntryPoint::class.java)