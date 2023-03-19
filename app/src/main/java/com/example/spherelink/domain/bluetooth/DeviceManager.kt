package com.example.spherelink.domain.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.example.spherelink.domain.TargetDevice

class DeviceManager(

    private val context: Context,
    targetDevices: List<TargetDevice>
) {
    private val targetDeviceMap: MutableMap<String, TargetDevice> = mutableMapOf()

    init {
        targetDevices.forEach { targetDevice ->
            targetDeviceMap[targetDevice.address] = targetDevice
        }
    }

    private val TAG = "com.example.spherelink.domain.bluetooth.DeviceManager"

    @SuppressLint("MissingPermission")
    fun connectAll() {
        Log.v(TAG, "Connecting to all devices")
        if (targetDeviceMap.isNotEmpty()) {
            val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter = bluetoothManager.adapter

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

        val gattCallback = MyGattCallback()
        // Implement your connection logic here
        device?.connectGatt(context, true, gattCallback)
    }




    private inner class MyGattCallback : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            val device = gatt.device

            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.i(TAG, "Connected to ${device.name} - ${device.address}")
                    // Discover services after successful connection
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.i(TAG, "Disconnected from ${device.name} - ${device.address}")
                    gatt.close()
                }
                else -> {
                    Log.i(TAG, "Connection state changed: status=$status, newState=$newState")
                }
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

        // Add other callback methods here as needed
    }
}
