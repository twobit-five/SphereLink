package com.example.spherelink.ui.device_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

private const val METERS_IN_FEET = 3.28084

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DeviceDetailsScreen(
    onPopBackStack: () -> Unit,
    viewModel: DeviceDetailsViewModel = hiltViewModel()
) {

    val deviceHistoryList = viewModel.history.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()
    val device = viewModel.device.collectAsState(initial = null)
    val deviceStats = viewModel.stats.collectAsState(initial = null)

    Scaffold(
        scaffoldState = scaffoldState,
        ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Upper part

            DeviceInfoCard(device = device.value)

            DeviceStatCard(deviceRssiStats = deviceStats.value)

            DeviceHistoryCard(deviceHistoryList = deviceHistoryList)
        }
    }
}

