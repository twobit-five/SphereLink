package com.example.spherelink.ui.device_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.spherelink.data.entities.DeviceEntity
import androidx.compose.ui.graphics.Color


@Composable
fun DeviceCard(
    device: DeviceEntity,
    onEvent: (DeviceListEvent) -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp
) {
    Card(
        modifier = modifier
            .background(Color.Blue, RoundedCornerShape(cornerRadius)),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "device.name",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = device.address,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Distance: ${device.distance} ft",
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "RSSI: ${device.rssi}",
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "Last Seen: ${device.timestamp} sec ago",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            IconButton(onClick = {
                onEvent(DeviceListEvent.onDeleteDeviceClick(device))
            }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewDeviceCard(){
    DeviceCard(
        device = DeviceEntity(
            address = "00:00:00:00:00",
            rssi = -50,
            distance = 10,
            timestamp = 0
        ),
        onEvent = {}
    )
}
