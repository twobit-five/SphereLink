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
private val baseRssi = -58
private val alphaWeight = 0.4

private const val LIMIT = 10


class DistanceCalculatorImpl @Inject constructor(private val repository: DeviceRepository) : DistanceCalculator {

    override fun calculateDistance(deviceAddress: String, currentRSSI: Int) {

        CoroutineScope(Dispatchers.IO).launch {

            val distance = 10.0.pow((baseRssi - currentRSSI) / (10 * attenValue))
            val oldDistance = repository.getDistance(deviceAddress)
            Log.d(TAG, "Old Distance for device $deviceAddress: $oldDistance")

            val newDistance = ((alphaWeight * distance) + ((1 - alphaWeight) * oldDistance)).toInt()
            Log.d(TAG, "New Distance calculated for device $deviceAddress: $newDistance")

            //TODO filter for values beyond a certain standard deviation from the mean?

            repository.insertRssiValueWithLimit(
                RssiValue(timestamp = System.currentTimeMillis(), deviceAddress = deviceAddress, rssi =  currentRSSI), LIMIT)

            repository.updateDistance(deviceAddress, newDistance)
            repository.updateRssi(deviceAddress, currentRSSI)
        }
    }
}