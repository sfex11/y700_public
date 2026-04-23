package com.gemmaton.cursor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(android.widget.FrameLayout(this))

        requestOverlayPermission()

        if (hasOverlayPermission()) {
            startOverlayService()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasOverlayPermission()) {
            startOverlayService()
            Toast.makeText(this, "Overlay service started", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun hasOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasOverlayPermission()) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, OVERLAY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_REQUEST_CODE) {
            if (hasOverlayPermission()) {
                startOverlayService()
                Toast.makeText(this, "Overlay permission granted, service started", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Overlay permission required", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    companion object {
        private const val OVERLAY_REQUEST_CODE = 1001
    }
}
