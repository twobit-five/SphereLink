package com.example.spherelink.domain.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.util.Log
import com.example.spherelink.domain.repo.DeviceRepository
import com.example.spherelink.domain.distance.RssiUpdater
import kotlinx.coroutines.*
import java.util.*

private const val TAG = "GattCallbackHandler"


class GattCallbackHandler(repository: DeviceRepository) : BluetoothGattCallback() {

    private val repository = repository

    private val BATTERY_SERVICE_UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb")
    private val BATTERY_LEVEL_UUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb")

    private var batteryLevel: Int = -1
    private val rssiUpdater = RssiUpdater(repository)

    @SuppressLint("MissingPermission")
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        val deviceAddress = gatt.device.address
        when (newState) {
            BluetoothProfile.STATE_CONNECTED -> {
                val scope = CoroutineScope(Job() + Dispatchers.Main)
                scope.launch(Dispatchers.IO) {
                    val deviceAddress = gatt.device.address
                    val deviceName = gatt.device.name
                    repository.updateDeviceEntitiyIsConnected(deviceAddress, true)

                    if (deviceName != null) {
                            repository.updateDeviceEntityDeviceName(deviceAddress, deviceName)
                    }
                }

                Log.i(TAG, "Device connected to GATT server. ${deviceAddress}")
                gatt.discoverServices()
                gatt.readPhy() // nto needed, but would like to implmenent LE Coded Phy for more reliable RSSI, but less throughput.
            }
            BluetoothProfile.STATE_DISCONNECTED -> {
                val scope = CoroutineScope(Job() + Dispatchers.Main)
                scope.launch(Dispatchers.IO) {
                    val deviceAddress = gatt.device.address
                    repository.updateDeviceEntitiyIsConnected(deviceAddress, false)
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
                    repository.updateDeviceEntityBatteryLevel(deviceAddress!!, batteryLevel)
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
                repository.updateDeviceEntityBatteryLevel(deviceAddress!!, batteryLevel)
            }

            Log.v(TAG, "Device: [${deviceAddress}], Battery level: $batteryLevel" )
        }
    }


    override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
        super.onReadRemoteRssi(gatt, rssi, status)

        if (status == BluetoothGatt.GATT_SUCCESS) {
            val deviceAddress = gatt?.device?.address
            //Log.d(TAG, "Remote RSSI for device $deviceAddress: $rssi")

            //update the rssi value in the database
            rssiUpdater.updateRssi(deviceAddress!!, rssi)

        } else {
            Log.d(TAG, "Read remote RSSI failed: $status")
        }
    }

    override fun onPhyRead(gatt: BluetoothGatt?, txPhy: Int, rxPhy: Int, status: Int) {
        super.onPhyRead(gatt, txPhy, rxPhy, status)
        Log.d(TAG, "onPhyRead: $txPhy, $rxPhy, $status")
    }
}