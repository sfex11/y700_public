package com.gemmaton.cursor

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class OverlayService : Service() {

    private var socketServer: SocketServer? = null
    private var fingerOverlay: FingerOverlay? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())

        fingerOverlay = FingerOverlay(this)
        socketServer = SocketServer(9999) { command ->
            handleCommand(command)
        }
        socketServer?.start()

        Log.d(TAG, "OverlayService started")
    }

    override fun onDestroy() {
        super.onDestroy()
        socketServer?.stop()
        fingerOverlay?.remove()
        Log.d(TAG, "OverlayService stopped")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun handleCommand(command: String) {
        Log.d(TAG, "Received command: $command")
        val trimmed = command.trim()
        when {
            trimmed == "clear" -> {
                fingerOverlay?.hide()
            }
            trimmed.startsWith("tap,") -> {
                // Format: tap,x,y
                val coords = trimmed.removePrefix("tap,").split(",")
                if (coords.size == 2) {
                    try {
                        val x = coords[0].trim().toFloat()
                        val y = coords[1].trim().toFloat()
                        fingerOverlay?.showAt(x, y)
                        // Auto-clear after 0.5 seconds
                        fingerOverlay?.postDelayed({ fingerOverlay?.hide() }, 500)
                    } catch (e: NumberFormatException) {
                        Log.e(TAG, "Invalid tap coords: $trimmed", e)
                    }
                }
            }
            else -> {
                // Format: x,y
                val parts = trimmed.split(",")
                if (parts.size == 2) {
                    try {
                        val x = parts[0].trim().toFloat()
                        val y = parts[1].trim().toFloat()
                        fingerOverlay?.showAt(x, y)
                    } catch (e: NumberFormatException) {
                        Log.e(TAG, "Invalid coords: $trimmed", e)
                    }
                } else {
                    Log.w(TAG, "Unknown command: $trimmed")
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Overlay Cursor Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Keeps the overlay cursor service running"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Overlay Cursor")
            .setContentText("Listening on port 9999")
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    companion object {
        private const val TAG = "OverlayService"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "overlay_cursor_channel"
    }
}
