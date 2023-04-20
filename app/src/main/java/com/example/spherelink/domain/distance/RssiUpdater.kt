package com.example.spherelink.domain.distance

import com.example.spherelink.data.entities.RssiValue
import com.example.spherelink.domain.repo.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DEVICE_HISTORY_LIMIT = 10

class RssiUpdater  @Inject constructor(private val repository: DeviceRepository) {

    //TODO updte this class to filter out the rssi values that are too far away from the average.
    fun updateRssi(deviceAddress: String, currentRSSI: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertRssiValueWithLimit(
                RssiValue(timestamp = System.currentTimeMillis(), deviceAddress = deviceAddress, rssi =  currentRSSI), DEVICE_HISTORY_LIMIT)

            repository.updateRssi(deviceAddress, currentRSSI)
        }
    }
}
