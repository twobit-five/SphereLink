package com.example.spherelink.domain.distance

import android.util.Log
import com.example.spherelink.data.entities.RssiValue
import com.example.spherelink.data.repository.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow

private const val TAG = "DistanceCalculatorImpl"

private const val attenValue = 2.4
// Calibration constant for RSSI at 1 meter
private const val baseRssi = -58
private const val alphaWeight = 0.4

private const val LIMIT = 10


class DistanceCalculatorImpl @Inject constructor(private val repository: DeviceRepository) : DistanceCalculator {

    override fun calculateDistance(deviceAddress: String, currentRSSI: Int) {

        CoroutineScope(Dispatchers.IO).launch {

            // TODO some devices send the calibration constant in the manufacturer data, use that instead of the default value
            val distance = 10.0.pow((baseRssi - currentRSSI) / (10 * attenValue))
            val oldDistance = repository.getDistance(deviceAddress)
            val newDistance = ((alphaWeight * distance) + ((1 - alphaWeight) * oldDistance)).toInt()
            Log.d(TAG, "Device: [$deviceAddress], Previous Distance [$oldDistance], Updated Distance [$newDistance]")

            //TODO filter for values beyond a certain standard deviation from the mean?

            // Insert new rssi value into database, for history.
            repository.insertRssiValueWithLimit(
                RssiValue(timestamp = System.currentTimeMillis(), deviceAddress = deviceAddress, rssi =  currentRSSI), LIMIT)

            // update distance and rssi in database, for the device card (display purposes)
            repository.updateDistance(deviceAddress, newDistance)
            repository.updateRssi(deviceAddress, currentRSSI)
        }
    }
}