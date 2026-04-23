package com.gemmaton.cursor

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView

class FingerOverlay(context: Context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val fingerView: ImageView

    private val layoutParams: WindowManager.LayoutParams

    private var isVisible = false

    init {
        fingerView = ImageView(context).apply {
            setImageResource(R.drawable.ic_finger)
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        layoutParams = WindowManager.LayoutParams(
            80,  // width
            120, // height
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 0
        }
    }

    fun showAt(screenX: Float, screenY: Float) {
        try {
            layoutParams.x = screenX.toInt()
            layoutParams.y = screenY.toInt()

            if (!isVisible) {
                windowManager.addView(fingerView, layoutParams)
                isVisible = true
                Log.d(TAG, "Finger shown at ($screenX, $screenY)")
            } else {
                windowManager.updateViewLayout(fingerView, layoutParams)
                Log.d(TAG, "Finger moved to ($screenX, $screenY)")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error showing finger overlay", e)
        }
    }

    fun hide() {
        if (isVisible) {
            try {
                windowManager.removeView(fingerView)
                isVisible = false
                Log.d(TAG, "Finger hidden")
            } catch (e: Exception) {
                Log.e(TAG, "Error hiding finger overlay", e)
            }
        }
    }

    fun remove() {
        hide()
    }

    fun postDelayed(runnable: Runnable, delayMillis: Long) {
        fingerView.postDelayed(runnable, delayMillis)
    }

    companion object {
        private const val TAG = "FingerOverlay"
    }
}
