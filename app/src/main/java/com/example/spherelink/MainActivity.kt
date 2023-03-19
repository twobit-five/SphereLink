package com.example.spherelink

import com.example.spherelink.domain.bluetooth.DeviceManager
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.spherelink.domain.PermissionHandler
import com.example.spherelink.domain.PermissionHandlerComposables
import com.example.spherelink.domain.TargetDevice
import com.example.spherelink.domain.bluetooth.BluetoothBroadcastReceiver
import com.example.spherelink.ui.theme.SphereLinkTheme

val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    //need to return if bluetooth is not supported
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val permissionHandler = PermissionHandler(this)

    // Initialize com.example.spherelink.domain.bluetooth.DeviceManager with a list of predetermined devices
    private val deviceManager = DeviceManager(
        this,
        listOf(
            TargetDevice("SphereLink-5467", "98:F4:AB:6D:6E:CE"),
            TargetDevice("Device-2", "00:11:22:33:44:56"),
            TargetDevice("Device-3", "00:11:22:33:44:57")
        )
    )

    private val bluetoothBroadcastReceiver = BluetoothBroadcastReceiver(this, permissionHandler, deviceManager)

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (bluetoothAdapter == null) {
            // Bluetooth is not supported on this device, handle this case
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            // Request to turn on Bluetooth if permission is granted
            if (permissionHandler.checkPermissions()) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(enableBtIntent)
            }
        }


        // Register Bluetooth broadcast receiver
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
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PermissionHandlerComposables(
                        permissionHandler = permissionHandler,
                        onPermissionsGranted = {
                            deviceManager.connectAll()
                        }
                    )
                    Greeting("Android")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bluetoothBroadcastReceiver)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SphereLinkTheme {
        Greeting("Android")
    }
}
