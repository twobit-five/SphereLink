package com.example.spherelink.domain.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.util.Log
import com.example.spherelink.data.entities.RssiValue
import com.example.spherelink.domain.repo.DeviceRepository
import com.example.spherelink.domain.distance.DistanceCalculator
import com.example.spherelink.domain.distance.DistanceCalculatorImpl
import kotlinx.coroutines.*
import java.util.*

private const val TAG = "GattCallbackHandler"
private const val DEVICE_HISTORY_LIMIT = 10


class GattCallbackHandler(repository: DeviceRepository) : BluetoothGattCallback() {

    private val repository = repository

    private val BATTERY_SERVICE_UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb")
    private val BATTERY_LEVEL_UUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb")

    private var batteryLevel: Int = -1

    private var distanceCalculator: DistanceCalculator = DistanceCalculatorImpl(repository)

    @SuppressLint("MissingPermission")
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        val deviceAddress = gatt.device.address
        when (newState) {
            BluetoothProfile.STATE_CONNECTED -> {

                val scope = CoroutineScope(Job() + Dispatchers.Main)
                scope.launch(Dispatchers.IO) {
                    val deviceAddress = gatt.device.address
                    val deviceName = gatt.device.name
                    repository.updateIsConnected(deviceAddress, true)

                    if (deviceName != null) {
                            repository.updateDeviceName(deviceAddress, deviceName)
                    }
                }

                Log.i(TAG, "Device connected to GATT server. ${deviceAddress}")
                gatt.discoverServices()
            }
            BluetoothProfile.STATE_DISCONNECTED -> {
                val scope = CoroutineScope(Job() + Dispatchers.Main)
                scope.launch(Dispatchers.IO) {
                    val deviceAddress = gatt.device.address
                    repository.updateIsConnected(deviceAddress, false)
                }

                Log.i(TAG, "Device disconnected from GATT server. ${deviceAddress}")
            }
        }
    }
    @SuppressLint("MissingPermission")
    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            val batteryService = gatt?.getService(BATTERY_SERVICE_UUID)
            if (batteryService != null) {
                val batteryLevelCharacteristic = batteryService.getCharacteristic(BATTERY_LEVEL_UUID)
                if (batteryLevelCharacteristic != null) {
                    gatt.readCharacteristic(batteryLevelCharacteristic)
                }
            }
        } else {
            Log.d(TAG, "onServicesDiscovered received: $status")
        }
    }

    override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            val deviceAddress = gatt?.device?.address
            if (characteristic?.uuid == BATTERY_LEVEL_UUID) {
                batteryLevel =
                    characteristic?.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0)!!
                CoroutineScope(Dispatchers.IO).launch {
                    repository.updateBatteryLevel(deviceAddress!!, batteryLevel)
                }
                Log.v(TAG, "Device: [${deviceAddress}], Battery level: $batteryLevel" )
            }
        }
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
        if (characteristic?.uuid == BATTERY_LEVEL_UUID) {
            val deviceAddress = gatt?.device?.address
            batteryLevel =
                characteristic?.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0)!!

            CoroutineScope(Dispatchers.IO).launch {
                repository.updateBatteryLevel(deviceAddress!!, batteryLevel)
            }
            Log.v(TAG, "Device: [${deviceAddress}], Battery level: $batteryLevel" )
        }
    }


    override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
        super.onReadRemoteRssi(gatt, rssi, status)

        if (status == BluetoothGatt.GATT_SUCCESS) {
            val deviceAddress = gatt?.device?.address
            Log.d(TAG, "Remote RSSI for device $deviceAddress: $rssi")

            // Add Device RSSI to device history table
            CoroutineScope(Dispatchers.IO).launch {
                repository.insertRssiValueWithLimit(
                    RssiValue(timestamp = System.currentTimeMillis(), deviceAddress = deviceAddress, rssi =  rssi), DEVICE_HISTORY_LIMIT)
            }

            //let the distance calculator do the work
            distanceCalculator.updateDistance(deviceAddress!!,rssi)

        } else {
            Log.d(TAG, "Read remote RSSI failed: $status")
        }
    }
}