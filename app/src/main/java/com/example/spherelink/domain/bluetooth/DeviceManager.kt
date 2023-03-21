package com.example.spherelink.domain.bluetooth

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.repository.DeviceRepository
import com.example.spherelink.domain.TargetDevice
import kotlinx.coroutines.*


//TODO this class is a mess, clean it up
// need to separate the logic for connecting to devices and the logic for reading/writing to devices
// THIS CLASS DOES TOO MUCH.
class DeviceManager private constructor(

    private val context: Context,
    targetDevices: List<TargetDevice>
) {
    private val TAG = "DeviceManager"
    private val MAX_RECONNECT_ATTEMPTS = 5

    private val targetDeviceMap: MutableMap<String, TargetDevice> = mutableMapOf()
    private val gattCallbackMap: MutableMap<String, MyGattCallback> = mutableMapOf() // Map to store the callback for each device
    private val gattMap: MutableMap<String, BluetoothGatt> = mutableMapOf()
    private val reconnectAttemptsMap: MutableMap<String, Int> = mutableMapOf()

    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter

    init {
        targetDevices.forEach { targetDevice ->
            targetDeviceMap[targetDevice.address] = targetDevice
            gattCallbackMap[targetDevice.address] = MyGattCallback()
            reconnectAttemptsMap[targetDevice.address] = 0
        }
    }

    companion object {
        @Volatile
        private var instance: DeviceManager? = null

        fun getInstance(context: Context, targetDevices: List<TargetDevice>): DeviceManager {
            return instance ?: synchronized(this) {
                instance ?: DeviceManager(context, targetDevices).also { instance = it }
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun connectAll() {

        Log.v(TAG, "Connecting to all devices")
        if (targetDeviceMap.isNotEmpty()) {

            targetDeviceMap.forEach { (_, targetDevice) ->
                val device = bluetoothAdapter.getRemoteDevice(targetDevice.address)
                Log.v(TAG, "Connecting to ${device.name} - ${device.address}")
                connectToDevice(device)
            }
        }
        else {
            Log.i(TAG, "No devices to connect to")
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice) {
        val gattCallback = gattCallbackMap[device.address]
        if (gattCallback == null) {
            Log.e(TAG, "No callback found for device ${device.address}")
            return
        }
        val gatt = device.connectGatt(context, false, gattCallback)
        gattMap[device.address] = gatt
    }

    @SuppressLint("MissingPermission")
    fun isDeviceConnected(device: BluetoothDevice): Boolean {
        val device = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT).find { it.address == device.address }
        val isConnected = (device != null)
        return isConnected
    }


    @SuppressLint("MissingPermission")
    fun requestRssiFromAll() {
        //Log.v(TAG, "Requesting RSSI for all devices")
        if (targetDeviceMap.isNotEmpty()) {
            targetDeviceMap.forEach { (_, targetDevice) ->
                val device = bluetoothAdapter.getRemoteDevice(targetDevice.address)
                requestRssiFromDevice(device)
            }
        } else {
            Log.i(TAG, "No devices to request RSSI from")
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestRssiFromDevice(device: BluetoothDevice) {
        val gatt = gattMap[device.address]
        if (isDeviceConnected(device)) {
            gatt?.readRemoteRssi()
        } else {
            Log.v(TAG, "${device.name} - ${device.address} not connected, skipping RSSI request")
        }
    }


    //TODO I dont like this as an inner class. I think it should be a separate class
    // add to the list of things to clean up, after basic functionality is working.
    private inner class MyGattCallback : BluetoothGattCallback() {

        private val deviceRepo = DeviceRepository(context)

        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            Log.i(TAG, "onConnectionStateChange: status: $status")

            val device = gatt.device
            if (status == BluetoothGatt.GATT_SUCCESS)
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        Log.i(TAG, "Connected to ${device.name} - ${device.address}")
                        // Discover services after successful connection
                        gatt.discoverServices()

                        reconnectAttemptsMap[device.address] = 0


                        // Update UI on the main thread
                        (context as Activity).runOnUiThread {
                            // Update UI elements here
                        }

                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        Log.i(TAG, "Disconnected from ${device.name} - ${device.address}")

                        // Update UI on the main thread
                        (context as Activity).runOnUiThread {
                            // Update UI elements here
                        }

                        gatt.close()

                        // Check if we have reached the maximum number of reconnection attempts
                        val reconnectAttempts = reconnectAttemptsMap[device.address] ?: 0
                        if (reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
                            Log.v(TAG, "Attempting to reconnect to ${device.name} - ${device.address} (attempt ${reconnectAttempts + 1})")
                            // Increment the reconnection attempt count
                            reconnectAttemptsMap[device.address] = reconnectAttempts + 1
                            // Attempt to reconnect
                            connectToDevice(device)
                        } else {
                            Log.v(TAG, "Reached maximum reconnection attempts for ${device.name} - ${device.address}")
                        }
                    }
                    else -> {
                        Log.i(TAG, "Connection state changed: status=$status, newState=$newState")
                    }
            } else {
                Log.e(TAG, "Failed to connect to ${device.name} - ${device.address}: status=$status")
            }
        }


        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            val device = gatt.device

            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Services discovered, you can interact with the device here
                Log.i(TAG, "Services discovered for ${device.name} - ${device.address}")
            } else {
                Log.w(TAG, "onServicesDiscovered received: $status")
            }
        }

        @SuppressLint("MissingPermission")
        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            super.onReadRemoteRssi(gatt, rssi, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val device = gatt?.device
                // RSSI value received successfully

                //TODO this smells!!!!
                //Should not be creating a new scope for every coroutine.
                //Create Scope once and reuse it. Tie it to the lifecycle of the activity

                // Insert RSSI value into database with a coroutine optimized for IO operations
                val scope = CoroutineScope(Job() + Dispatchers.Main)
                scope.launch(Dispatchers.IO) {
                    val deviceEntity = DeviceEntity(device!!.address, rssi)
                    deviceRepo.insertDevice(deviceEntity)
                }

                Log.d(TAG, "Remote RSSI value: $rssi for ${device?.name} - ${device?.address}")
            } else {
                Log.w(TAG, "Failed to read remote RSSI value: status=$status")
            }
        }
    }
}
