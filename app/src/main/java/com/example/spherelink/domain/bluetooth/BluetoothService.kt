package com.example.spherelink.domain.bluetooth

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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


//TODO should probably make this service a singleton. :)
@AndroidEntryPoint
class BluetoothService (): Service() {

    private val TAG = "BluetoothService"

    @Inject
    lateinit var deviceManager: DeviceManager

    @Inject
    lateinit var repository: DeviceRepository

    private val bluetoothBroadcastReceiver = BluetoothBroadcastReceiver()

    private val delay = 10000L // 10 seconds
    private val notificationId = 501 // unique ID for the notification

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        deviceManager = DeviceManager(this, repository)

        // Register Bluetooth broadcast receiver
        val intentFilter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        }
        registerReceiver(bluetoothBroadcastReceiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start the service in the foreground
        Log.v(TAG,"BluetoothService is running in the foreground")
        startForeground(notificationId, createNotification())

        // Launch a coroutine to call the suspend function
        job = coroutineScope.launch {
            Log.v(TAG,"BluetoothService isActive: ${job?.isActive}")
            while (isActive) {
                val deviceList: List<DeviceEntity> = repository.getDevicesAsList()
                deviceManager.setDeviceList(deviceList)
                deviceManager.connectToAllDevices()
                deviceManager.requestRSSIfromAllDevices()

                // TODO add a variable delay which increases sample rate when in motion?
                // Good starting point for static frequency is between 1-10 samples per second.
                delay(delay)
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Disconnect from all devices and release the resources
        deviceManager.disconnectFromAllDevices()

        // Cancel the coroutine job when the service is destroyed
        job?.cancel()

        // Stop the service as a foreground service and remove the notification
        stopForeground(true)
        unregisterReceiver(bluetoothBroadcastReceiver)
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
            .setContentTitle("Bluetooth Service")
            .setContentText("Running...")
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        return notification
    }
}
