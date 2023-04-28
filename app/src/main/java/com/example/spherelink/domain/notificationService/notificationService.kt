package com.example.spherelink.domain.notificationService

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
import com.example.spherelink.R
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.domain.repo.DeviceRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : Service() {

    @Inject
    lateinit var repository: DeviceRepository

    private val TAG = "NotificationService"

    private val maxDistance = 2
    private val delay = 10000L // 5 seconds
    private val notificationId = 502 // unique ID for the notification

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")
        // Start the service in the foreground

        startForeground(notificationId, createNotification())

        // Launch a coroutine to call the suspend function
        job = coroutineScope.launch {
            while (isActive) {
                val deviceList: List<DeviceEntity> = repository.getDevicesAsList()
                Log.d(TAG, "Retrieved device list: $deviceList")

                for (device in deviceList) {
                    val distance = repository.getDistance(device.address)

                    if (distance > maxDistance) {
                        Log.d(TAG, "Device out of range: ${device.device_name} (${device.address})")
                        createDistanceNotification(device)
                    }
                }

                delay(delay)
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed")

        // Cancel the coroutine job when the service is destroyed
        job?.cancel()

        // Stop the service as a foreground service and remove the notification
        stopForeground(true)
    }

    private fun createNotification(): Notification {
        // Create a notification to show in the status bar
        val notificationChannelId = "ForegroundServiceChannelId"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
        }

        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Notification Service")
            .setContentText("Running...")
            .setSmallIcon(R.drawable.ic_notification)
            .build()
    }

    private fun createDistanceNotification(device: DeviceEntity) {
        Log.d(TAG, "Creating distance notification for device: ${device.device_name} (${device.address})")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "DistanceNotificationChannel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Distance Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)

            .setContentTitle("Device Out of Range")
            .setContentText("${device.device_name} is more than $maxDistance meters away.")
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(device.address.hashCode(), notification)
    }
}