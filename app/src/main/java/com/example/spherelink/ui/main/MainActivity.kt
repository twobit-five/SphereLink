package com.example.spherelink.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spherelink.ui.theme.SphereLinkTheme
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.spherelink.domain.bluetooth.BluetoothService
import com.example.spherelink.domain.notificationservice.NotificationService
import com.example.spherelink.ui.permission.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBluetoothServiceRunning = false
    lateinit var  navController: NavHostController

    private val permissionsToRequest = arrayOf(
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.POST_NOTIFICATIONS,
        //Manifest.permission.ACCESS_FINE_LOCATION,
        //Manifest.permission.ACCESS_COARSE_LOCATION
        Manifest.permission.CAMERA
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SphereLinkTheme {

                val viewModel = viewModel<PermissionViewModel>()
                val dialogQueue = viewModel.visiblePermissionDialogQueue

                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms ->
                        permissionsToRequest.forEach { permission ->
                            viewModel.onPermissionResult(
                                permission = permission,
                                isGranted = perms[permission] == true
                            )
                        }
                    }
                )

                navController = rememberNavController()
                SetupNavGraph(navController)

                LaunchedEffect(Unit) {
                    multiplePermissionResultLauncher.launch(permissionsToRequest)
                }

                dialogQueue
                    .reversed()
                    .forEach { permission ->
                        PermissionDialog(
                            permissionTextProvider = when (permission) {
                                Manifest.permission.BLUETOOTH_ADMIN -> {
                                    BluetoothAdminPermissionTextProvider()
                                }
                                Manifest.permission.BLUETOOTH -> {
                                    BluetoothPermissionTextProvider()
                                }
                                Manifest.permission.BLUETOOTH_CONNECT -> {
                                    BluetoothConnectPermissionTextProvider()
                                }
                                Manifest.permission.ACCESS_FINE_LOCATION -> {
                                    FineLocationPermissionTextProvider()
                                }
                                Manifest.permission.ACCESS_COARSE_LOCATION -> {
                                    CourseLocationPermissionTextProvider()
                                }
                                Manifest.permission.POST_NOTIFICATIONS -> {
                                    CourseLocationPermissionTextProvider()
                                }
                                else -> return@forEach
                            },
                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                permission
                            ),
                            onDismiss = viewModel::dismissDialog,
                            onOkClick = {
                                viewModel.dismissDialog()
                                multiplePermissionResultLauncher.launch(
                                    arrayOf(permission)
                                )
                            },
                            onGoToAppSettingsClick = ::openAppSettings
                        )
                    }
            }
        }

        // Start the foreground service
        val intent = Intent(this, BluetoothService::class.java)
        ContextCompat.startForegroundService(this, intent)
        isBluetoothServiceRunning = true

        val notificationServiceIntent = Intent(this, NotificationService::class.java)
        ContextCompat.startForegroundService(this, notificationServiceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isBluetoothServiceRunning) {
            stopBluetoothService()
        }

        // Stop the notification service
        val notificationServiceIntent = Intent(this, NotificationService::class.java)
        stopService(notificationServiceIntent)
    }

    private fun stopBluetoothService() {
        val intent = Intent(this, BluetoothService::class.java)
        stopService(intent)
        isBluetoothServiceRunning = false
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

