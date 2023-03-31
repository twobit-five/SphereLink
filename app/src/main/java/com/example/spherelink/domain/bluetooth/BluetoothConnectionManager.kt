package com.example.spherelink.domain.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log

class BluetoothConnectionManager(private val context: Context, private val callback: BluetoothGattCallback) {

    private val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private var bluetoothGatt: BluetoothGatt? = null

    companion object {
        private const val TAG = "BluetoothLeHelper"
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(macAddress: String) {
        val device = bluetoothAdapter?.getRemoteDevice(macAddress)

        if (device != null) {
            bluetoothGatt = device.connectGatt(context, false, callback)
            Log.i(TAG, "Connecting to device: $macAddress")
        } else {
            Log.e(TAG, "Device not found: $macAddress")
        }
    }

    @SuppressLint("MissingPermission")
    fun isDeviceConnected(): Boolean {
        return bluetoothGatt?.connect() ?: false
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {
        bluetoothGatt?.disconnect()
    }
}