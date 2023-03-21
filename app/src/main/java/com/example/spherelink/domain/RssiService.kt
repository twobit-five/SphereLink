package com.example.spherelink.domain

import android.app.Service
import android.content.Intent
import android.os.IBinder

import android.util.Log
import com.example.spherelink.domain.bluetooth.DeviceManager

class RssiService() : Service() {

    private val TAG = "DeviceManagerService"


    //TODO Populate this some how and keep it in sync with the database

    //TODO this is horrible, but it works for now.
    // Eventually this list will be created from a database.
    // Will need to continously sync the database with this list.
    // So we aren't requesting RSSI's for devices that are no longer in the database.
    val targetDevices = listOf(
        TargetDevice("SphereLink-5467", "98:F4:AB:6D:6E:CE")
        //TargetDevice("Device 2", "00:11:22:33:44:56"),
        //TargetDevice("Device 3", "00:11:22:33:44:57")
    )

    private lateinit var deviceManager: DeviceManager
    private lateinit var thread: Thread
    // Initialize DeviceManager

    override fun onCreate() {
        super.onCreate()
        deviceManager = DeviceManager.getInstance(this, targetDevices)
    }

    override fun onBind(intent: Intent?): IBinder? {
        // No binding needed, return null
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Do some background work here
        Log.v(TAG, "Service started")

        deviceManager.connectAll()

        thread = Thread {
            while (true) {
                Log.v(TAG, "Requesting RSSIs")
                deviceManager.requestRssiFromAll()
                Thread.sleep(10000) //sleep for 10 seconds
            }
        }

        // Stop the service when the task is complete
        stopSelf()

        thread.start()
        return START_STICKY
    }



    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "Service Killed")
    }
}
