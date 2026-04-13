package com.gemmaton.cursor

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class SocketServer(
    private val port: Int,
    private val onCommand: (String) -> Unit
) {
    private var serverSocket: ServerSocket? = null
    private var running = false
    private var clientSocket: Socket? = null

    @Synchronized
    fun start() {
        if (running) return
        running = true
        thread(name = "SocketServer") {
            try {
                serverSocket = ServerSocket(port)
                Log.d(TAG, "Listening on port $port")
                while (running) {
                    try {
                        clientSocket = serverSocket?.accept()
                        Log.d(TAG, "Client connected: ${clientSocket?.inetAddress}")
                        val reader = BufferedReader(InputStreamReader(clientSocket?.getInputStream()))
                        var line: String?
                        while (running && clientSocket?.isClosed == false) {
                            line = reader.readLine() ?: break
                            onCommand(line)
                        }
                    } catch (e: Exception) {
                        if (running) {
                            Log.e(TAG, "Client connection error", e)
                        }
                    } finally {
                        try {
                            clientSocket?.close()
                        } catch (_: Exception) {
                        }
                        clientSocket = null
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Server error", e)
            } finally {
                running = false
            }
        }
    }

    @Synchronized
    fun stop() {
        running = false
        try {
            clientSocket?.close()
        } catch (_: Exception) {
        }
        try {
            serverSocket?.close()
        } catch (_: Exception) {
        }
    }

    companion object {
        private const val TAG = "SocketServer"
    }
}
