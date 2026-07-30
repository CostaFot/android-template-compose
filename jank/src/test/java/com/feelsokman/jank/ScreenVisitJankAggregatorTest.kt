package com.feelsokman.jank

import androidx.metrics.performance.FrameData
import androidx.metrics.performance.StateInfo
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Tests [ScreenVisitJankAggregator].
 */
class ScreenVisitJankAggregatorTest {

    private val reports = mutableListOf<ScreenVisitJank>()
    private var timeMs = 0L
    private val aggregator = ScreenVisitJankAggregator(
        reporter = { reports += it },
        clock = { timeMs },
    )

    @Test
    fun framesAccumulateUntilExplicitFlush() {
        repeat(10) { aggregator.onFrame(frame(screen = "RouteA", durationMs = 8)) }
        aggregator.onFrame(frame(screen = "RouteA", durationMs = 40, isJank = true))
        assertEquals(emptyList(), reports)

        timeMs = 500
        aggregator.flush(reason = "app_background")

        val report = reports.single()
        assertEquals("RouteA", report.screen)
        assertEquals(500, report.visitDurationMs)
        assertEquals(11, report.totalFrames)
        assertEquals(1, report.jankFrames)
        assertEquals(0, report.frozenFrames)
        assertEquals("app_background", report.flushReason)
    }

    @Test
    fun navigationChangeFlushesPreviousVisit() {
        aggregator.onFrame(frame(screen = "RouteA", durationMs = 8))
        aggregator.onFrame(frame(screen = "RouteB", durationMs = 8))

        val report = reports.single()
        assertEquals("RouteA", report.screen)
        assertEquals(1, report.totalFrames)
        assertEquals("navigation", report.flushReason)
    }

    @Test
    fun navigationKeyArgumentsAreStripped() {
        aggregator.onFrame(frame(screen = "RouteB(id=42)", durationMs = 8))
        aggregator.flush(reason = "app_background")

        assertEquals("RouteB", reports.single().screen)
    }

    @Test
    fun navigationKeyDefaultToStringIsNormalizedToTypeName() {
        aggregator.onFrame(
            frame(
                screen = "com.feelsokman.androidtemplate.ui.activity.RouteA@b705e52",
                durationMs = 8,
            ),
        )
        aggregator.flush(reason = "app_background")

        assertEquals("RouteA", reports.single().screen)
    }

    @Test
    fun qualifiedNavigationKeyWithArgumentsIsNormalizedToTypeName() {
        aggregator.onFrame(
            frame(screen = "com.example.feature.topic.RouteB(id=42)", durationMs = 8),
        )
        aggregator.flush(reason = "app_background")

        assertEquals("RouteB", reports.single().screen)
    }

    @Test
    fun framesWithoutNavigationStateReportUnknownScreen() {
        aggregator.onFrame(FrameData(0, 8_000_000, false, emptyList()))
        aggregator.flush(reason = "app_background")

        assertEquals("unknown", reports.single().screen)
    }

    @Test
    fun scrollingFramesAreAttributedSeparately() {
        aggregator.onFrame(frame(screen = "RouteA", durationMs = 8))
        aggregator.onFrame(frame(screen = "RouteA", durationMs = 8, isScrolling = true))
        aggregator.onFrame(
            frame(screen = "RouteA", durationMs = 40, isJank = true, isScrolling = true),
        )
        aggregator.flush(reason = "app_background")

        val report = reports.single()
        assertEquals(3, report.totalFrames)
        assertEquals(2, report.scrollFrames)
        assertEquals(1, report.jankFrames)
        assertEquals(1, report.scrollJankFrames)
    }

    @Test
    fun frozenFramesAreCounted() {
        aggregator.onFrame(frame(screen = "RouteA", durationMs = 800, isJank = true))
        aggregator.flush(reason = "app_background")

        val report = reports.single()
        assertEquals(1, report.jankFrames)
        assertEquals(1, report.frozenFrames)
    }

    @Test
    fun overrunDataIsAbsentForPreApi31FrameData() {
        aggregator.onFrame(frame(screen = "RouteA", durationMs = 40, isJank = true))
        aggregator.flush(reason = "app_background")

        val report = reports.single()
        assertNull(report.maxFrameOverrunMs)
        assertNull(report.jankFrameOverrunBucketsMs)
    }

    @Test
    fun flushWithoutFramesReportsNothing() {
        aggregator.flush(reason = "app_background")
        aggregator.onFrame(frame(screen = "RouteA", durationMs = 8))
        aggregator.flush(reason = "app_background")
        aggregator.flush(reason = "app_background")

        assertEquals(1, reports.size)
    }

    private fun frame(
        screen: String,
        durationMs: Long,
        isJank: Boolean = false,
        isScrolling: Boolean = false,
    ): FrameData = FrameData(
        0,
        durationMs * 1_000_000,
        isJank,
        buildList {
            add(StateInfo(NAVIGATION_STATE_KEY, screen))
            if (isScrolling) {
                add(StateInfo("mainScreen:list", SCROLLING_STATE_VALUE))
            }
        },
    )
}
