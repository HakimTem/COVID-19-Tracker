package com.zerodeveloper.covid19_infotracker.ui

import android.app.Activity
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity


    fun fullscreenActivity(activity: Activity, view: View) {
        if (activity is AppCompatActivity) {
            activity.supportActionBar?.hide()
        } else {
            activity.actionBar?.hide()
        }
        val mHideHandler = Handler()
        val mHideRunnable = Runnable {
            view.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        val mShowRunnable = Runnable {
            if (activity is AppCompatActivity) {
                activity.supportActionBar?.show()
            } else {
                activity.actionBar?.show()
            }
        }

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowRunnable)
        mHideHandler.postDelayed(mHideRunnable, 10.toLong())
    }
