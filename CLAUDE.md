# Claude Instructions

- Do not ask for confirmation before taking actions. Proceed autonomously.

## Jank tracking (`:jank` module)

Frame performance monitoring built on `androidx.metrics:metrics-performance` (JankStats),
adapted from the Now in Android sample. Data flow: `JankStats` reports per-frame data to
`ScreenVisitJankAggregator`, which aggregates it into one `ScreenVisitJank` report per screen
visit and hands it to a `JankReporter` (default: `LogcatJankReporter`, one JSON line per visit
under logcat tag `JankReport`). Frames are attributed to navigation destinations via
`PerformanceMetricsState`; the state-key contract is internal to the module.

To wire an activity (see `MainActivity` for the reference setup):

- Inject `dagger.Lazy<JankStats>` and `dagger.Lazy<ScreenVisitJankAggregator>` (provided by
  `JankStatsModule`, activity-scoped).
- `onResume`: `lazyStats.get().isTrackingEnabled = true`.
- `onPause`: flush the aggregator (reason `"activity_pause"`), then disable tracking.
- In Compose: call `NavigationTrackingSideEffect(currentNavKey)` with the current back stack
  key (any type; attributed via `toString()`), and `TrackScrollJank(listState, "screen:list")`
  on scrollables.

To ship reports to a real backend, replace the `JankReporter` binding in `JankStatsModule`.
