package com.example.spherelink.ui.device_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.spherelink.data.entities.DeviceRssiHistory


@Composable
fun DeviceHistoryCard(
    deviceHistoryList: State<List<DeviceRssiHistory>>,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    )
{
    //TODO add device history title
    Card(
        modifier = Modifier
            //.weight(1f)
            .fillMaxWidth()
            .border(width = 2.dp, color = Color.Black)
            .background(Color.Blue, RoundedCornerShape(cornerRadius))
        ) {
        LazyColumn {
            items(deviceHistoryList.value) { history ->
                Text(text = "Timestamp: ${history.timestamp}, RSSI: ${history.rssi}")
            }
        }
    }
}