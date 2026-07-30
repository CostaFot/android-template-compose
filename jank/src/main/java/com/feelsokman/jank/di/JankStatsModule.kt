package com.feelsokman.jank.di

import android.app.Activity
import android.view.Window
import androidx.metrics.performance.JankStats
import androidx.metrics.performance.JankStats.OnFrameListener
import com.feelsokman.jank.JankReporter
import com.feelsokman.jank.LogcatJankReporter
import com.feelsokman.jank.ScreenVisitJankAggregator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object JankStatsModule {
    @Provides
    fun providesJankReporter(): JankReporter = LogcatJankReporter()

    @Provides
    @ActivityScoped
    fun providesScreenVisitJankAggregator(
        reporter: JankReporter,
    ): ScreenVisitJankAggregator = ScreenVisitJankAggregator(reporter)

    @Provides
    fun providesOnFrameListener(
        aggregator: ScreenVisitJankAggregator,
    ): OnFrameListener = OnFrameListener(aggregator::onFrame)

    @Provides
    fun providesWindow(activity: Activity): Window = activity.window

    @Provides
    fun providesJankStats(
        window: Window,
        frameListener: OnFrameListener,
    ): JankStats = JankStats.createAndTrack(window, frameListener)
}
