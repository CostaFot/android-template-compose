package com.feelsokman.androidtemplate.jank

import android.util.Log
import org.json.JSONObject

/**
 * [JankReporter] that emits each screen visit as a single structured JSON log line.
 *
 * This stands in for a real log pipeline: an app shipping its logs to a backend
 * (e.g. Datadog) would write the same JSON line through its file/network logger
 * instead of logcat. One line per screen visit keeps volume proportional to
 * navigation events rather than rendered frames.
 */
class LogcatJankReporter : JankReporter {

    override fun report(visit: ScreenVisitJank) {
        val json = JSONObject()
            .put("event", "screen_visit_jank")
            .put("screen", visit.screen)
            .put("visit_duration_ms", visit.visitDurationMs)
            .put("total_frames", visit.totalFrames)
            .put("jank_frames", visit.jankFrames)
            .put("frozen_frames", visit.frozenFrames)
            .put("scroll_frames", visit.scrollFrames)
            .put("scroll_jank_frames", visit.scrollJankFrames)
            .put("flush_reason", visit.flushReason)
        visit.maxFrameOverrunMs?.let { json.put("max_frame_overrun_ms", it) }
        visit.jankFrameOverrunBucketsMs?.let { json.put("jank_frame_overrun_ms", JSONObject(it)) }
        Log.i(TAG, json.toString())
    }

    companion object {
        private const val TAG = "JankReport"
    }
}
