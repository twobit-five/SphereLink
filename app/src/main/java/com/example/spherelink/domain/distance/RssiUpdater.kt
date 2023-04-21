package com.example.spherelink.domain.distance

import com.example.spherelink.data.entities.RssiValue
import com.example.spherelink.domain.repo.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Math.abs
import javax.inject.Inject

private const val DEVICE_HISTORY_LIMIT = 10
private const val RSSI_STANDARD_DEVIATION_LIMIT = 2
private const val RSSI_TIME_THRESHOLD = 5000L

class RssiUpdater  @Inject constructor(private val repository: DeviceRepository) {
    fun updateRssi(deviceAddress: String, currentRSSI: Int) {
        CoroutineScope(Dispatchers.IO).launch {

            //clear old rssi values with threshold
            repository.deleteOldRssiValues(deviceAddress, System.currentTimeMillis() - RSSI_TIME_THRESHOLD)

            //add new rssi value to database blindly
            repository.insertRssiValueWithLimit(
                RssiValue(
                    timestamp = System.currentTimeMillis(),
                    deviceAddress = deviceAddress,
                    rssi =  currentRSSI), DEVICE_HISTORY_LIMIT)
            repository.updateRssi(deviceAddress, currentRSSI)

            val deviceHistory = repository.getDeviceHistoryList(deviceAddress)
            val averageRssi = deviceHistory.map { it.rssi }.average()
            val rssiStandardDeviations = deviceHistory.map {
                (it.rssi - averageRssi) / deviceHistory.size.toDouble()
            }
            // Create a list of RSSI values to remove based on the filtering criteria.
            val rssiValuesToRemove = deviceHistory.filter { history ->
                val rssiStandardDeviation = rssiStandardDeviations[deviceHistory.indexOf(history)]
                abs(history.rssi - averageRssi) > (RSSI_STANDARD_DEVIATION_LIMIT * rssiStandardDeviation)
            }

            // Remove the RSSI values that are outside the standard deviation limit.
            for (rssiValue in rssiValuesToRemove) {
                repository.deleteRssiValues(rssiValue.id)
            }
        }
    }
}
