package com.example.spherelink.domain.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.example.spherelink.data.entities.DeviceEntity
import javax.inject.Inject

class BluetoothConnectionManager @Inject constructor(private val context: Context) {

    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    private val gattCallbackMap: MutableMap<String, GattCallbackHandler> = mutableMapOf() // Map to store the callback for each device
    private val gattMap: MutableMap<String, BluetoothGatt> = mutableMapOf()


    companion object {
        private const val TAG = "BluetoothLeHelper"
    }

    fun setDeviceList(targetDeviceList: List<DeviceEntity>) {
        updateDeviceList(targetDeviceList)
    }

    fun updateDeviceList(newDeviceList: List<DeviceEntity>) {

        for (device in newDeviceList) {
            gattCallbackMap.clear()
            if (!gattCallbackMap.containsKey(device.address)) {
                gattCallbackMap[device.address] = GattCallbackHandler()
            }
        }
    }

    fun connectToAllDevices() {
        //ghatt callback map is a map of mac address to gatt callback
        Log.v(TAG, "connectToAllDevices: ${gattCallbackMap.keys}")

        gattCallbackMap.keys.forEach { macAddress ->
            if (!isDeviceConnected(macAddress))
                connectToDevice(macAddress)
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(macAddress: String) {
        val device = bluetoothAdapter?.getRemoteDevice(macAddress)
        if (device != null) {
            val gattCallback = gattCallbackMap[macAddress]
            if (gattCallback != null) {
                val gatt = device.connectGatt(context, false, gattCallback)
                gattMap[macAddress] = gatt
            } else {
                Log.e(TAG, "No GattCallback found for device: $macAddress")
            }
        } else {
            Log.e(TAG, "Device not found for address: $macAddress")
        }
    }

    @SuppressLint("MissingPermission")
    fun isDeviceConnected(address: String): Boolean {
        return gattMap[address]?.device?.bondState == BluetoothDevice.BOND_BONDED
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {
        //TODO: Implement this
    }
}