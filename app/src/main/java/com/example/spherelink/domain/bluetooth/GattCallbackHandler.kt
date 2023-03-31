package com.example.spherelink.domain.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.util.Log

class GattCallbackHandler: BluetoothGattCallback() {
    private val TAG = "GattCallbackHandler"

    @SuppressLint("MissingPermission")
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        when (newState) {
            BluetoothProfile.STATE_CONNECTED -> {
                Log.i(TAG, "Connected to GATT server.")
                Log.i(TAG, "Attempting to start service discovery: ${gatt.discoverServices()}")
            }
            BluetoothProfile.STATE_DISCONNECTED -> {
                Log.i(TAG, "Disconnected from GATT server.")
            }
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            gatt.services.forEach { service: BluetoothGattService ->
                // Handle discovered services
            }
        } else {
            Log.w(TAG, "onServicesDiscovered received: $status")
        }
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        // Handle characteristic read
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        // Handle characteristic changed
    }
}