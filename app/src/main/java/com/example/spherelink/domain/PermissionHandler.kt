package com.example.spherelink.domain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class PermissionHandler(
    private val context: Context,
) {

    companion object {
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,

            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }

    fun checkPermissions(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

}

@Composable
fun PermissionHandlerComposables(
    permissionHandler: PermissionHandler,
    onPermissionsGranted: () -> Unit
) {
    val context = LocalContext.current

    // Use the REQUIRED_PERMISSIONS constant here
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            onPermissionsGranted()
        } else {
            // At least one permission denied, show a message or disable functionality
            val missingPermissions = permissions.filter { !it.value }.map { it.key }
            val message = "The following permissions are required: ${missingPermissions.joinToString()}"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(permissionHandler) {
        if (!permissionHandler.checkPermissions()) {
            requestPermissionLauncher.launch(PermissionHandler.REQUIRED_PERMISSIONS)
        }
    }
}
