package com.amalcodes.dyahacademy.android.analytics

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


class AnalyticsLifecycleCallback(
    private val analytics: Analytics
) : FragmentManager.FragmentLifecycleCallbacks(),
    Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStarted(activity: Activity) {
        if (activity is TrackScreen) {
            analytics.trackScreen(activity, activity.screenName)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(this, true)
        }
    }

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        if (f is TrackScreen) {
            analytics.trackScreen(f.requireActivity(), f.screenName)
        }
    }

}