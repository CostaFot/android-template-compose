package com.feelsokayman.template

import com.android.build.api.dsl.CommonExtension

/**
 * Configure project for Gradle managed devices
 */
internal fun configureGradleManagedDevices(
    commonExtension: CommonExtension,
) {
    val mediumPhone = DeviceConfig("Medium Phone", 30, "aosp-atd")

    val allDevices = listOf(mediumPhone)

    commonExtension.testOptions.apply {
        managedDevices.apply {
            localDevices.apply {
                allDevices.forEach { deviceConfig ->
                    maybeCreate(deviceConfig.taskName).apply {
                        device = deviceConfig.device
                        apiLevel = deviceConfig.apiLevel
                        systemImageSource = deviceConfig.systemImageSource
                    }
                }
            }
        }
    }
}

private data class DeviceConfig(
    val device: String,
    val apiLevel: Int,
    val systemImageSource: String,
) {
    val taskName = buildString {
        append(device.lowercase().replace(" ", ""))
        append("api")
        append(apiLevel.toString())
        append(systemImageSource.replace("-", ""))
    }
}
