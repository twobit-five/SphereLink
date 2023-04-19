package com.example.spherelink.domain.distance

import android.util.Log
import com.example.spherelink.data.dao.DeviceDao
import com.example.spherelink.data.entities.RssiValue
import com.example.spherelink.domain.repo.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.abs
import kotlin.math.sign

private const val TAG = "DistanceCalculatorImpl"

private const val attenValue = 2.4
//TODO  Calibration constant for RSSI at 1 meter to increase accuracy.
//NEED TO GET THIS FROM THE DEVICE, represented as TX power
private const val baseRssi = -58
private const val alphaWeight = 0.4

private const val LIMIT = 10


class DistanceCalculatorImpl @Inject constructor(private val repository: DeviceRepository) : DistanceCalculator {

    override fun calculateDistance(deviceAddress: String, currentRSSI: Int) {

        CoroutineScope(Dispatchers.IO).launch {

            //TODO some devices send the calibration constant in the manufacturer data, use that instead of the default value
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


//    //calculateDistance2 ignores distances outside a standard deviation of the historical distances.
//    //values outside of this are ignored.
//    override fun calculateDistance(deviceAddress: String, currentRSSI: Int) {
//        CoroutineScope(Dispatchers.IO).launch {
//
//            val distance = 10.0.pow((baseRssi - currentRSSI) / (10 * attenValue))
//            val oldDistance = repository.getDistance(deviceAddress)
//
//            //Calculate the standard deviation of the historical distances
//            val rssiList = repository.getDeviceHistoryList(deviceAddress).map {it.rssi}
//            val distanceList = rssiList.map {10.0.pow(baseRssi - it) / (10 * attenValue)}
//            val meanDistance = distanceList.average()
//            val stdDevDistance = sqrt(distanceList.map { it - meanDistance}.map {it * it}.sum()/rssiList.size)
//
//            //Compare |distance - oldDistance| to standard deviation
//            if (abs(distance - oldDistance) < stdDevDistance) {
//                Log.d(TAG, "Device: [$deviceAddress], Previous Distance [$oldDistance], Updated Distance [$distance]")
//                repository.insertRssiValueWithLimit(
//                    RssiValue(timestamp = System.currentTimeMillis(), deviceAddress = deviceAddress, rssi = currentRSSI), LIMIT)
//
//                repository.updateDistance(deviceAddress, distance.toInt())
//                repository.updateRssi(deviceAddress, currentRSSI)
//            } else {
//                Log.d(TAG, "Device: [$deviceAddress], new distance is anomalous.")
//            }
//
//        }
//    }

//    //calculateDistance3 is the same as calculateDistance2, but accounts for recurring distances.
//    //values outside of this are ignored.
//    override fun calculateDistance(deviceAddress: String, currentRSSI: Int) {
//        CoroutineScope(Dispatchers.IO).launch {
//
//            val distance = 10.0.pow((baseRssi - currentRSSI) / (10 * attenValue))
//            val oldDistance = repository.getDistance(deviceAddress)
//
//            //Calculate the standard deviation of the historical distances.
//            //Set a threshold for the incoming distances.
//            val rssiList = repository.getDeviceHistoryList(deviceAddress).map {it.rssi}
//            val distanceList = rssiList.map {10.0.pow(baseRssi - it) / (10 * attenValue)}
//            val meanDistance = distanceList.average()
//            val stdDevDistance = sqrt(distanceList.map { it - meanDistance}.map {it * it}.sum()/rssiList.size)
//            val stdDevThreshold = 3
//
//            //Compare |distance - oldDistance| to standard deviation + threshold
//            if (abs(distance - oldDistance) < stdDevDistance + stdDevThreshold) {
//                Log.d(TAG, "Device: [$deviceAddress], Previous Distance [$oldDistance], Updated Distance [$distance]")
//                repository.insertRssiValueWithLimit(
//                    RssiValue(timestamp = System.currentTimeMillis(), deviceAddress = deviceAddress, rssi = currentRSSI), LIMIT)
//
//                repository.updateDistance(deviceAddress, distance.toInt())
//                repository.updateRssi(deviceAddress, currentRSSI)
//            } else {
//                Log.d(TAG, "Device: [$deviceAddress], new distance is anomalous.")
//            }
//
//        }
//    }

//    //calculateDistance4 is the same as calculateDistance3, but accounts for recurring distances.
//    //values outside of this are dampened to that threshold.
//    override fun calculateDistance(deviceAddress: String, currentRSSI: Int) {
//        CoroutineScope(Dispatchers.IO).launch {
//
//            val distance = 10.0.pow((baseRssi - currentRSSI) / (10 * attenValue))
//            val oldDistance = repository.getDistance(deviceAddress)
//
//            //Calculate the standard deviation of the historical distances.
//            //Set a threshold for the incoming distances.
//            val rssiList = repository.getDeviceHistoryList(deviceAddress).map {it.rssi}
//            val distanceList = rssiList.map {10.0.pow(baseRssi - it) / (10 * attenValue)}
//            val meanDistance = distanceList.average()
//            val stdDevDistance = sqrt(distanceList.map { it - meanDistance}.map {it * it}.sum()/rssiList.size)
//            val stdDevThreshold = 3
//            val sign = if (distance - oldDistance >= 0) 1 else -1
//
//            //Compare |distance - oldDistance| to standard deviation + threshold.
//            //Posts distance if within that threshold, posts limit otherwise.
//            if (abs(distance - oldDistance) < stdDevDistance + stdDevThreshold) {
//                Log.d(TAG, "Device: [$deviceAddress], Previous Distance [$oldDistance], Updated Distance [$distance]")
//                repository.insertRssiValueWithLimit(
//                    RssiValue(timestamp = System.currentTimeMillis(), deviceAddress = deviceAddress, rssi = currentRSSI.toInt()), LIMIT)
//
//                repository.updateDistance(deviceAddress, distance.toInt())
//                repository.updateRssi(deviceAddress, currentRSSI)
//            } else {
//                Log.d(TAG, "Device: [$deviceAddress], Distance out of Range, Previous Distance [$oldDistance], Updated Distance[$distance]")
//                repository.insertRssiValueWithLimit(
//                    RssiValue(timestamp = System.currentTimeMillis(), deviceAddress = deviceAddress, rssi = currentRSSI.toInt()), LIMIT)
//
//                repository.updateDistance(deviceAddress, oldDistance + sign * stdDevThreshold)
//                repository.updateRssi(deviceAddress, currentRSSI)
//            }
//
//        }
//    }
}