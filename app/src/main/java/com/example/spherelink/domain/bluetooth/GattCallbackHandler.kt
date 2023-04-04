package com.example.spherelink.domain.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.util.Log
import com.example.spherelink.domain.distance.DistanceCalculator
import javax.inject.Inject

class GattCallbackHandler: BluetoothGattCallback() {
    private val TAG = "GattCallbackHandler"

    private  lateinit var  distanceCalculator: DistanceCalculator

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


    override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
        super.onReadRemoteRssi(gatt, rssi, status)


        //caclulate distance from current rssi
        //distanceCalculator.calculateDistance(rssi)

        // do caluclation for distance on past and current

    }
}