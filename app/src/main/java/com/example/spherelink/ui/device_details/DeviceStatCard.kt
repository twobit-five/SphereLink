package com.example.spherelink.ui.device_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.spherelink.data.entities.DeviceRssiStats
import com.example.spherelink.ui.device_list.DeviceListEvent

@Composable
fun DeviceStatCard(
    deviceRssiStats: DeviceRssiStats?,
    //onEvent: (DeviceListEvent) -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
) {
    //TODO add device stat title
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Blue, RoundedCornerShape(cornerRadius)),
        elevation = 8.dp
    ) {
        Column {
            Text(
                text = "Avg RSSI: ${deviceRssiStats?.avgRssi}",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Std Dev: ${deviceRssiStats?.stdDev}",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Variance: ${deviceRssiStats?.variance}",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Median: ${deviceRssiStats?.median}",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Range: ${deviceRssiStats?.range}",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Min: ${deviceRssiStats?.min}",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Max: ${deviceRssiStats?.max}",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Mode: ${deviceRssiStats?.mode}",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Count: ${deviceRssiStats?.count}",
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DeviceStatCardPreview() {
    DeviceStatCard(
        deviceRssiStats = DeviceRssiStats(
            deviceAddress = "00:00:00:00:00:00",
            avgRssi = -50,
            stdDev = 1.0,
            variance = 1.0,
            median = -50,
            range = 1,
            min = -50,
            max = -50,
            mode = -50,
            count = 49
        ),
        //onEvent = {}
    )
}
