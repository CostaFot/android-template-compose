package com.feelsokman.jank

// Contract between the composables that write PerformanceMetricsState
// (NavigationTrackingSideEffect, TrackScrollJank) and ScreenVisitJankAggregator,
// which reads that state back from each frame.

/** State key holding the current navigation destination. */
internal const val NAVIGATION_STATE_KEY = "Navigation"

/** Prefix marking an active scroll, regardless of which scrollable's state name wrote it. */
internal const val SCROLLING_STATE_PREFIX = "Scrolling"

/** State value written by [TrackScrollJank] while a scroll is in progress. */
internal const val SCROLLING_STATE_VALUE = "$SCROLLING_STATE_PREFIX=true"
