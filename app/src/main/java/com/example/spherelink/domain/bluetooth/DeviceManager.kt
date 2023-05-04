package com.example.spherelink.domain.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.domain.distance.DistanceCalculator
import com.example.spherelink.domain.repo.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class DeviceManager @Inject constructor(private val context: Context, private val repository: DeviceRepository) {

    private val TAG = "DeviceManager"

    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    private val gattCallback: GattCallbackHandler = GattCallbackHandler(repository)
    private val gattMap: MutableMap<String, BluetoothGatt> = ConcurrentHashMap()
    private var distanceCalculator: DistanceCalculator = DistanceCalculator(repository)


    fun connectToAllDevices(deviceList : List<DeviceEntity>) {
        for (device in deviceList) {
            if (!isDeviceConnected(device.address)) {
                connectToDevice(device.address)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(address: String): Boolean {
        bluetoothAdapter?.let { adapter ->
            try {
                Log.v(TAG, "Attempting to connect to $address")

                val device = adapter.getRemoteDevice(address)
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

    // TODO remove from map if not connected?
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
        }
    }

    @SuppressLint("MissingPermission")
    private fun disconnectDevice(macAddress: String) {
        val gatt = gattMap[macAddress]
        if (gatt != null) {
            gatt.disconnect()
            gatt.close()
            gattMap.remove(macAddress)
        }
    }

    fun disconnectFromAllDevices() {
        gattMap.keys.forEach { macAddress ->
            disconnectDevice(macAddress)
        }
    }

    fun updateDistances() {
        CoroutineScope(Dispatchers.IO).launch {
            val deviceList = repository.getDeviceEntitiesAsList()
            deviceList.forEach { device ->
                if(isDeviceConnected(device.address)) {
                    distanceCalculator.updateDistance(device.address)
                }
            }
        }
    }
}