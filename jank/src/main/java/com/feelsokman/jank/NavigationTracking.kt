package com.feelsokman.jank

import androidx.compose.runtime.Composable

/**
 * Stores the current navigation destination in
 * [androidx.metrics.performance.PerformanceMetricsState] so that [ScreenVisitJankAggregator]
 * can attribute frames to screens.
 *
 * [currentNavKey] is attributed via its `toString()`, so any navigation key type works;
 * the aggregator normalizes the value to the key's bare type name.
 */
@Composable
fun NavigationTrackingSideEffect(currentNavKey: Any) {
    TrackDisposableJank(currentNavKey) { metricsHolder ->
        metricsHolder.state?.putState(NAVIGATION_STATE_KEY, currentNavKey.toString())
        onDispose {}
    }
}
