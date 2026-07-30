package com.feelsokman.jank

/**
 * Aggregated jank data for a single visit to one screen (one navigation destination
 * staying on screen). Counts are sent instead of ratios so that a backend can compute
 * fleet-wide rates correctly as sum(jankFrames) / sum(totalFrames) — averaging
 * per-device ratios would weight a 2-frame visit the same as a 10,000-frame one.
 */
data class ScreenVisitJank(
    /** Navigation key of the destination, with any arguments stripped. */
    val screen: String,
    val visitDurationMs: Long,
    val totalFrames: Int,
    /** Frames exceeding the jank heuristic (default: 2x the refresh-rate frame deadline). */
    val jankFrames: Int,
    /** Frames slower than 700ms, which users perceive as a freeze. */
    val frozenFrames: Int,
    /** Frames produced while any scrollable reported an active scroll. */
    val scrollFrames: Int,
    val scrollJankFrames: Int,
    /** Worst frame overrun past its deadline. Null below API 31 where overrun is unavailable. */
    val maxFrameOverrunMs: Long?,
    /**
     * Histogram of overrun for janky frames, keyed by upper bound in ms
     * ("le_16" .. "gt_700"). Null below API 31.
     */
    val jankFrameOverrunBucketsMs: Map<String, Int>?,
    /** Why the report was emitted: "navigation", "activity_pause" or "buffer_limit". */
    val flushReason: String,
)

/**
 * Sink for aggregated jank reports. Implementations forward reports to whatever
 * logging or telemetry pipeline the app uses.
 */
fun interface JankReporter {
    /**
     * Called with one report per screen visit. Note: invoked on the JankStats
     * delivery thread — implementations doing I/O should hand off to their own executor.
     */
    fun report(visit: ScreenVisitJank)
}
