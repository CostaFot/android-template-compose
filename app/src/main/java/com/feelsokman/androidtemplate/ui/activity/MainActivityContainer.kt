package com.feelsokman.androidtemplate.ui.activity

import com.feelsokman.common.NetworkMonitor
import dagger.Lazy
import javax.inject.Inject

class MainActivityContainer {

    @Inject
    lateinit var networkMonitor: Lazy<NetworkMonitor>
}