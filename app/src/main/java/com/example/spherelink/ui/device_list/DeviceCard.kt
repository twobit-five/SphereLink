package com.example.spherelink.ui.device_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.spherelink.data.entities.DeviceEntity
import androidx.compose.ui.graphics.Color
import java.time.Duration
import java.time.Instant


@Composable
fun DeviceCard(
    device: DeviceEntity,
    onEvent: (DeviceListEvent) -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
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
                    text = device.name,
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
                    text = "Distance: ${device.distance} m",
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "RSSI: ${device.rssi}, Avg: ${device.avgRSSI}",
                    modifier = Modifier.padding(top = 8.dp)
                )
                val lastSeenDuration = Duration.between(
                    Instant.ofEpochMilli(device.timestamp),
                    Instant.now()
                )
                Text(
                    text = "Last seen: ${formatDuration(lastSeenDuration)}",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            IconButton(onClick = {
                onEvent(DeviceListEvent.OnDeleteDeviceClick(device))
            }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete"
                )
            }
            Icon(
                imageVector = if (device.isConnected) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                tint = if (device.isConnected) Color.Green else Color.Gray,
                contentDescription = if (device.isConnected) "Connected" else "Not connected",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private fun formatDuration(duration: Duration): String {
    val days = duration.toDays()
    val hours = duration.toHours() % 24
    val minutes = duration.toMinutes() % 60

    return when {
        days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
        hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
        minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
        else -> "just now"
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDeviceCard(){
    DeviceCard(
        device = DeviceEntity(
            address = "00:00:00:00:00",
            name = "Test Device",
            rssi = -50,
            distance = 10,
            timestamp = System.currentTimeMillis() - 9000,
            isDone = false,
            isConnected = true,
            batteryLevel = 100,
            avgRSSI = 0
        ),
        onEvent = {}
    )
}