package com.example.spherelink.domain.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.spherelink.domain.PermissionHandler

class BluetoothBroadcastReceiver(context: Context,
                                 private val permissionHandler: PermissionHandler,
                                 private val deviceManager: DeviceManager
) : BroadcastReceiver() {

    private val TAG = "BluetoothBroadcastReceiver"

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        when (action) {
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        // Bluetooth turned off
                        Toast.makeText(context, "Bluetooth turned off", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Bluetooth turned off")
                        // Request to turn on Bluetooth if permission is granted
                        if (permissionHandler.checkPermissions()) {
                            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                            enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(enableBtIntent)
                        }
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        // Bluetooth turning off
                        Toast.makeText(context, "Bluetooth turning off", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Bluetooth turning off")
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> {
                        // Bluetooth turning on
                        Toast.makeText(context, "Bluetooth turning on", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Bluetooth turning on")
                    }
                    BluetoothAdapter.STATE_ON -> {
                        // Bluetooth turned on
                        Toast.makeText(context, "Bluetooth turned on", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Bluetooth turned on")
                        // Connect to devices when Bluetooth is turned on
                        deviceManager.connectAll()
                    }
                }
            }


            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                // Device connected
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    Toast.makeText(context, "Device connected: ${it.name} - ${it.address}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Device connected: ${it.name} - ${it.address}")
                }
            }

            BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                // Device bond state changed
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    Toast.makeText(context, "Device bond state changed: ${it.name} - ${it.address}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Device bond state changed: ${it.name} - ${it.address}")
                }
            }

            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                // Device disconnected
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    Toast.makeText(context, "Device disconnected: ${it.name} - ${it.address}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Device disconnected: ${it.name} - ${it.address}")
                }
            }

            //may remove this as the app doesn't currently scan for devices
            BluetoothDevice.ACTION_FOUND -> {
                // Device found
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    Toast.makeText(context, "Device found: ${it.name} - ${it.address}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Device found: ${it.name} - ${it.address}")
                }
            }

            //may remove this as the app doesn't currently scan for devices
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                // Discovery finished
                Toast.makeText(context, "Discovery finished", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Discovery finished")
            }
        }
    }
}
