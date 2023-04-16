package com.example.spherelink.ui.device_details

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.entities.RssiValue

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DeviceDetailsScreen(
    onPopBackStack: () -> Unit,
    viewModel: DeviceDetailsViewModel = hiltViewModel()
) {

    val deviceHistoryList = viewModel.history.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()
    val device = viewModel.device

    Scaffold() {
        Column(modifier = Modifier.fillMaxSize()) {
            // Upper part
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Device Name: ${device?.device_name}")
                Text(text = "Device Address: ${device?.address}")
                Text(text = "RSSI: ${device?.rssi}")
                Text(text = "Distance: ${device?.distance}")
                Text(text = "Is Connected: ${device?.isConnected}")
                Text(text = "Timestamp: ${device?.timestamp}")
                Text(text = "Battery Level: ${device?.batteryLevel}%")
            }

            // Bottom part (wrapped in a border)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(width = 2.dp, color = Color.Black)
            ) {
                LazyColumn {
                    items(deviceHistoryList.value) { history ->
                        Text(text = "Timestamp: ${history.timestamp}, RSSI: ${history.rssi}")
                    }
                }
            }
        }
    }
}

