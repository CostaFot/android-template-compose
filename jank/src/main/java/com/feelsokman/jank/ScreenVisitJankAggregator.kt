package com.feelsokman.jank

import android.os.SystemClock
import androidx.metrics.performance.FrameData
import androidx.metrics.performance.FrameDataApi31
import androidx.metrics.performance.JankStats

/**
 * Aggregates per-frame [JankStats] data into one [ScreenVisitJank] report per screen visit,
 * so that report volume scales with navigation events instead of rendered frames.
 *
 * Frames are attributed to the current navigation destination through the "Navigation"
 * value in [FrameData.states] (see [NavigationTrackingSideEffect]). When that value
 * changes, the accumulated visit is flushed to the [reporter]. Callers should also
 * [flush] when the tracked activity pauses, since no further frames (and therefore no
 * further state changes) will arrive while it is not in the foreground.
 */
class ScreenVisitJankAggregator(
    private val reporter: JankReporter,
    private val clock: () -> Long = SystemClock::elapsedRealtime,
) {

    private val lock = Any()
    private var currentScreen: String? = null
    private var visitStartMs = 0L
    private var totalFrames = 0
    private var jankFrames = 0
    private var frozenFrames = 0
    private var scrollFrames = 0
    private var scrollJankFrames = 0
    private var maxFrameOverrunMs = 0L
    private var hasOverrunData = false
    private val jankOverrunBuckets = IntArray(OVERRUN_BUCKET_UPPER_BOUNDS_MS.size + 1)

    fun onFrame(frameData: FrameData) {
        // Extract everything before returning: JankStats reuses the FrameData instance
        // and its states list across frames.
        // Normalize the navigation key's toString to its bare type name; anything else
        // explodes the number of distinct screens on a backend:
        // - data-class keys render arguments: "RouteB(id=42)" -> "RouteB"
        // - keys without a toString override render as
        //   "com.example....RouteA@b705e52" (new identity hash every process)
        //   -> "RouteA"
        val screen = frameData.states.firstOrNull { it.key == NAVIGATION_STATE_KEY }
            ?.value
            ?.substringBefore('(')
            ?.substringBefore('@')
            ?.substringAfterLast('.')
            ?: UNKNOWN_SCREEN
        val isScrolling = frameData.states.any { it.value.startsWith(SCROLLING_STATE_PREFIX) }
        val isJank = frameData.isJank
        val durationMs = frameData.frameDurationUiNanos / NANOS_PER_MS
        val overrunMs = (frameData as? FrameDataApi31)?.let { it.frameOverrunNanos / NANOS_PER_MS }

        // The report is built under the lock but delivered outside it, so a slow
        // reporter can never make another thread (e.g. main calling flush from
        // onPause) wait on the lock for the duration of the report call.
        // At most one report per frame: a navigation flush resets totalFrames,
        // so the buffer limit cannot also trigger in the same call.
        var pendingReport: ScreenVisitJank? = null
        synchronized(lock) {
            if (screen != currentScreen) {
                pendingReport = buildReportAndResetLocked(reason = "navigation")
                currentScreen = screen
                visitStartMs = clock()
            }
            totalFrames++
            if (isScrolling) scrollFrames++
            if (isJank) {
                jankFrames++
                if (isScrolling) scrollJankFrames++
                if (durationMs >= FROZEN_FRAME_THRESHOLD_MS) frozenFrames++
                if (overrunMs != null) {
                    hasOverrunData = true
                    if (overrunMs > maxFrameOverrunMs) maxFrameOverrunMs = overrunMs
                    jankOverrunBuckets[bucketIndex(overrunMs)]++
                }
            }
            if (totalFrames >= MAX_FRAMES_PER_REPORT) {
                pendingReport = buildReportAndResetLocked(reason = "buffer_limit")
            }
        }
        pendingReport?.let(reporter::report)
    }

    /**
     * Emits a report for the visit accumulated so far and resets the counters. The current
     * screen is kept, so the visit continues accumulating if more frames arrive afterwards.
     */
    fun flush(reason: String) {
        val report = synchronized(lock) {
            buildReportAndResetLocked(reason)
        }
        report?.let(reporter::report)
    }

    private fun buildReportAndResetLocked(reason: String): ScreenVisitJank? {
        val screen = currentScreen
        val report = if (screen != null && totalFrames > 0) {
            ScreenVisitJank(
                screen = screen,
                visitDurationMs = clock() - visitStartMs,
                totalFrames = totalFrames,
                jankFrames = jankFrames,
                frozenFrames = frozenFrames,
                scrollFrames = scrollFrames,
                scrollJankFrames = scrollJankFrames,
                maxFrameOverrunMs = if (hasOverrunData) maxFrameOverrunMs else null,
                jankFrameOverrunBucketsMs = if (hasOverrunData) {
                    OVERRUN_BUCKET_LABELS.withIndex()
                        .associate { (index, label) -> label to jankOverrunBuckets[index] }
                } else {
                    null
                },
                flushReason = reason,
            )
        } else {
            null
        }
        visitStartMs = clock()
        totalFrames = 0
        jankFrames = 0
        frozenFrames = 0
        scrollFrames = 0
        scrollJankFrames = 0
        maxFrameOverrunMs = 0
        hasOverrunData = false
        jankOverrunBuckets.fill(0)
        return report
    }

    private fun bucketIndex(overrunMs: Long): Int {
        OVERRUN_BUCKET_UPPER_BOUNDS_MS.forEachIndexed { index, upperBound ->
            if (overrunMs <= upperBound) return index
        }
        return OVERRUN_BUCKET_UPPER_BOUNDS_MS.size
    }

    companion object {
        /** Screen reported for frames rendered before the first navigation state arrives. */
        private const val UNKNOWN_SCREEN = "unknown"

        /** Threshold above which a frame counts as frozen, matching Play vitals. */
        private const val FROZEN_FRAME_THRESHOLD_MS = 700

        /** Flush long-lived visits periodically so a report is never unboundedly stale. */
        private const val MAX_FRAMES_PER_REPORT = 18_000

        private const val NANOS_PER_MS = 1_000_000

        private val OVERRUN_BUCKET_UPPER_BOUNDS_MS = longArrayOf(16, 50, 200, 700)
        private val OVERRUN_BUCKET_LABELS =
            listOf("le_16", "le_50", "le_200", "le_700", "gt_700")
    }
}
