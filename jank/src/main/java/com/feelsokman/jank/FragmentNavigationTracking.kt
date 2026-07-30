package com.feelsokman.jank

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.metrics.performance.PerformanceMetricsState

/**
 * Fragment counterpart of [NavigationTrackingSideEffect]: attributes frames to the most
 * recently resumed fragment for screens that are not driven by Compose navigation.
 *
 * Register once per activity, before fragments are added:
 * ```
 * supportFragmentManager.registerFragmentLifecycleCallbacks(
 *     NavigationTrackingFragmentCallbacks(),
 *     /* recursive = */ true,
 * )
 * ```
 *
 * With `recursive = true` child fragments (e.g. the current ViewPager page) win the
 * attribution over their host, since they resume last. The state is written with the
 * fragment's simple class name, so reports stay stable across process restarts; it is
 * deliberately not removed on pause — the next resumed fragment (or Compose destination)
 * overwrites it, matching how [NavigationTrackingSideEffect] behaves.
 */
class NavigationTrackingFragmentCallbacks : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        val view = f.view ?: return
        PerformanceMetricsState.getHolderForHierarchy(view)
            .state
            ?.putState(NAVIGATION_STATE_KEY, f.javaClass.simpleName)
    }
}
