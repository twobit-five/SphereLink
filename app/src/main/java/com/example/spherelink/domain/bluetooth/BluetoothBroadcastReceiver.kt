package com.example.spherelink.domain.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BluetoothBroadcastReceiver() : BroadcastReceiver() {

    private val TAG = "BluetoothBroadcastReceiver"


    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        when (action) {
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        // Bluetooth turned off
                        Toast.makeText(context, "Bluetooth turned off", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Bluetooth turned off")

                        //permission is not checked here.
                        // Request to turn on Bluetooth
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context?.startActivity(enableBtIntent)

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
                    }
                }
            }
        }
    }
}