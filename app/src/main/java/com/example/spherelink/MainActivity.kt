package com.example.spherelink

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.spherelink.domain.PermissionHandler
import com.example.spherelink.domain.bluetooth.BluetoothBroadcastReceiver
import com.example.spherelink.domain.RssiService
import com.example.spherelink.ui.theme.SphereLinkTheme
import android.net.Uri
import android.provider.Settings

val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    //need to return if bluetooth is not supported
    //TODO deperacated.  Need to update to use bluetoothManager
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    //private val permissionHandler = PermissionHandler(this, )
    private val permissionHandler by lazy {
        PermissionHandler(this, this)
    }

    //private val bluetoothBroadcastReceiver = BluetoothBroadcastReceiver(this, permissionHandler, deviceManager)
    //TODO reevaluate the usefuless of this class!!!
    private val bluetoothBroadcastReceiver = BluetoothBroadcastReceiver(this, permissionHandler)

    //TODO Can this be removed, since the service is started from an intent?
    private val rssiService = RssiService()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Bluetooth adapter is null, indicating that bluetooth is not supported on this device. EXIT
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        //Check if bluetooth is enabled, if not request to turn it on
        if (!bluetoothAdapter.isEnabled) {
            //Check if we have permission to turn on bluetooth
            if (permissionHandler.checkPermissions()) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(enableBtIntent)
            }
        }

        // Register Bluetooth broadcast receiver
        //TODO MAy remove some of these actions... We arent doing anything with half of these.
        // clean up your mess!!!!
        val intentFilter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED )
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            addAction(ACCESS_FINE_LOCATION)
            addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
        }
        registerReceiver(bluetoothBroadcastReceiver, intentFilter)


        setContent {
            SphereLinkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    //TODO REFACTOR THIS
                    if (!permissionHandler.checkPermissions()) {
                        permissionHandler.requestPermissions()
                    } else {


                        // All permissions granted, proceed with app logic

                        //TODO this is UGLY needs to be in permission handler
                        val intent = Intent()
                        intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                        intent.data = Uri.parse("package:" + applicationContext.packageName)
                        startActivity(intent)

                        //TODO This is discouraged in Android 8.0 (API level 26) and higher, for battery life concerns.
                        //Convert to foregroundservice, job scheduler, work manager
                        // Start the RssiService
                        //Starting the DeviceManagerService
                        Log.v(TAG, "Starting Rssi service")
                        val serviceIntent = Intent(this, RssiService::class.java)
                        startService(serviceIntent)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bluetoothBroadcastReceiver)
    }

    //TODO this is deprecated, need to update
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
