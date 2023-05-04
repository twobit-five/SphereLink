package com.example.spherelink.ui.device_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.spherelink.data.entities.DeviceEntity

@Composable
fun DeviceInfoCard(
    device: DeviceEntity?,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    ) {

    //TODO add device info title
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = Color.Black)
            .background(Color.Blue, RoundedCornerShape(cornerRadius)),
        ) {
        Column() {
            Text(
                text = "Device Name: ${device?.name}",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Device Address: ${device?.address}",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "RSSI: ${device?.rssi}",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Distance: ${device?.distance} m",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Is Connected: ${device?.isConnected}",
                modifier = Modifier.padding(4.dp)
            )
            //TODO convert timestamp to date/time format
            Text(
                text = "Timestamp: ${device?.timestamp}",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Battery Level: ${device?.batteryLevel}%",
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
