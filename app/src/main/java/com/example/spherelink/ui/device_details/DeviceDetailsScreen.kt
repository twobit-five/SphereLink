package com.example.spherelink.ui.device_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spherelink.util.UiEvent

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddDeviceScreen(
    onPopBackStack: () -> Unit,
    viewModel: DeviceDetailsViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }
                else -> Unit
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Device Details", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Device Address: ${viewModel.deviceAddress}")
            Text(text = "Device Name: ${viewModel.deviceName}")
            Text(text = "RSSI: ${viewModel.deviceRssi}")
            Text(text = "Distance: ${viewModel.deviceDistance}")
            Text(text = "Battery Level: ${viewModel.deviceBatteryLevel}")
            Text(text = "Last Seen: ${viewModel.deviceTimestamp}")
            Text(text = "Device Connected: ${viewModel.deviceIsConnected}")
        }
    }
}