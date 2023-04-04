package com.example.spherelink.domain.bluetooth

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
import com.example.spherelink.data.repository.DeviceRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.*

@AndroidEntryPoint
class BluetoothService (): Service() {

    private val TAG = "BluetoothService"
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null
    private var deviceList: List<DeviceEntity> = emptyList()

    @Inject
    lateinit var connectionManager: BluetoothConnectionManager

    @Inject
    lateinit var repository: DeviceRepository

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start the service in the foreground
        Log.v(TAG,"BluetoothService is running in the foreground")

        startForeground(1, createNotification())

        // Launch a coroutine to call the suspend function
        job = coroutineScope.launch {
            Log.v(TAG,"BluetoothService isActive: ${job?.isActive}")
            while (isActive) {
                // Collect the flow from getAllDevices() and process each emitted item
                repository.getAllDevices().collect { devices ->
                    // Update the device list
                    deviceList = devices
                    // Convert the list of devices to a list of device addresses
                    val deviceAddresses = devices.map { device -> device.address }
                    Log.v(TAG, "List of device addresses: $deviceAddresses")

                    //update the connection manager with the most current device list
                    connectionManager.setDeviceList(deviceList)

                    // Connect to all devices
                    connectionManager.connectToAllDevices()


                    delay(10000) //delay 10 seconds
                }
            }
        }

        Thread.sleep(10000)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the coroutine job when the service is destroyed
        job?.cancel()
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
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Foreground Service")
            .setContentText("Running...")
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        return notification
    }
}