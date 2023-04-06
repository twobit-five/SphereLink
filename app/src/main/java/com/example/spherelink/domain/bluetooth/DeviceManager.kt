package com.example.spherelink.domain.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.repository.DeviceRepository
import javax.inject.Inject


class DeviceManager @Inject constructor(private val context: Context, private val repository: DeviceRepository) {

    private val TAG = "DeviceManager"

    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    private val gattCallbackMap: MutableMap<String, GattCallbackHandler> = mutableMapOf() // Map to store the callback for each device
    private val gattMap: MutableMap<String, BluetoothGatt> = mutableMapOf()


    fun setDeviceList(targetDeviceList: List<DeviceEntity>) {
        updateDeviceList(targetDeviceList)
    }

    fun updateDeviceList(newDeviceList: List<DeviceEntity>) {

        for (device in newDeviceList) {
            if (!gattCallbackMap.containsKey(device.address)) {
                gattCallbackMap[device.address] = GattCallbackHandler(repository)
            }
        }
    }

    fun connectToAllDevices() {
        //ghatt callback map is a map of mac address to gatt callback
        Log.v(TAG, "connectToAllDevices: ${gattCallbackMap.keys}")

        gattCallbackMap.keys.forEach { address ->
            if (!isDeviceConnected(address))
                connectToDevice(address)
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(address: String): Boolean {
        bluetoothAdapter?.let { adapter ->
            try {
                Log.v(TAG, "Attempting to connect to $address")
                gattCallbackMap.put(address, GattCallbackHandler(repository))
                val gattCallback = gattCallbackMap[address]

                val device = adapter.getRemoteDevice(address)
                // connect to the GATT server on the device
                var bluetoothGatt = device.connectGatt(context, false, gattCallback)
                gattMap[address] = bluetoothGatt
                return true
            } catch (exception: IllegalArgumentException) {
                Log.w(TAG, "Device not found with provided address.  Unable to connect.")
                return false
            }
        } ?: run {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return false
        }
    }


    @SuppressLint("MissingPermission")
    fun isDeviceConnected(macAddress: String): Boolean {
        try {
            val device = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT).find { it.address == macAddress }
            val isConnected = (device != null)
            return isConnected
        } catch (e: Exception) {
            // Handle the exception appropriately
            Log.e(TAG, "Error in isDeviceConnected(): ${e.message}")
            return false
        }
    }


    fun requestRSSIfromAllDevices() {
        gattMap.keys.forEach { macAddress ->

            Log.v(TAG, "Attempting to request RSSI from $macAddress")
            val device = bluetoothAdapter?.getRemoteDevice(macAddress)

            if (device != null) {
                requestRssiFromDevice(device)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestRssiFromDevice(device: BluetoothDevice) {
        val gatt = gattMap[device.address]
        if (isDeviceConnected(device.address)) {
            gatt?.readRemoteRssi()
        } else {
            Log.v(TAG, "${device.name} - ${device.address} not connected, skipping RSSI request")
        }
    }

    @SuppressLint("MissingPermission")
    private fun disconnectDevice(macAddress: String) {
        val gatt = gattMap[macAddress]
        if (gatt != null) {
            gatt.disconnect()
            gatt.close()
            gattMap.remove(macAddress)
            gattCallbackMap.remove(macAddress)
        }
    }

    fun disconnectFromAllDevices() {
        gattMap.keys.forEach { macAddress ->
            disconnectDevice(macAddress)
        }
    }
}