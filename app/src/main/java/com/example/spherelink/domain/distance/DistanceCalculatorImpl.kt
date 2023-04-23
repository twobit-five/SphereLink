package com.example.spherelink.domain.distance

import android.util.Log
import com.example.spherelink.data.entities.RssiValue
import com.example.spherelink.domain.repo.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow

private const val TAG = "DistanceCalculatorImpl"

private const val attenValue = 2.4
//TODO  Calibration constant for RSSI at 1 meter to increase accuracy.
//NEED TO GET THIS FROM THE DEVICE, represented as TX power
private const val baseRssi = -34
private const val alphaWeight = 0.4


class DistanceCalculatorImpl @Inject constructor(private val repository: DeviceRepository) : DistanceCalculator {

    override fun updateDistance(deviceAddress: String) {
        CoroutineScope(Dispatchers.IO).launch {

            val deviceHistory = repository.getDeviceHistoryList(deviceAddress)
            val newDistance = calculateDistanceFromHistory(deviceHistory)

            Log.d(TAG,"Device: [$deviceAddress], Updated Distance [$newDistance]")

            // update distance and rssi in database, for the device card (display purposes)
            updateDistanceForDeviceEntity(deviceAddress, newDistance)
        }
    }

    suspend fun updateDistanceForDeviceEntity(deviceAddress: String, distance: Int) {
        repository.updateDistance(deviceAddress, distance)
    }

    fun calculateDistanceFromHistory(deviceHistory: List<RssiValue>): Int {
        var distCalc = 0
        var rssiSum = 0

         if (deviceHistory.isNotEmpty()) {
             for (rssiEntry in deviceHistory) {
                 rssiSum = (rssiSum + rssiEntry.rssi)
             }

             val avgRssi = rssiSum / deviceHistory.size
             distCalc = calculateDistance(avgRssi)
         }

        return distCalc
    }

    fun calculateDistance(rssi: Int): Int {
        return 10.0.pow((baseRssi - rssi) / (10 * attenValue)).toInt()
    }
}