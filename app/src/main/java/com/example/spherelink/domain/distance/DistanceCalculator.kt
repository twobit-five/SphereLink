package com.example.spherelink.domain.distance

import com.example.spherelink.data.entities.DeviceRssiHistory
import com.example.spherelink.data.entities.DeviceRssiStats
import com.example.spherelink.domain.repo.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow

private const val TAG = "DistanceCalculatorImpl"

//TODO improve the distance calculation, by adjusting these values to a specific environment
//by empiracally gathering data and adjusting the values to fit the data
private const val attenValue = 2.4
private const val baseRssi = -42


//TODO this class name no longer correctly describes what it does
//Refactor or rename?
class DistanceCalculator @Inject constructor(private val repository: DeviceRepository)  {

    fun updateDistance(deviceAddress: String) {
        CoroutineScope(Dispatchers.IO).launch {

            //TODO some of these calculations use the same data, can we optimize? Do we need to?
            val deviceHistory = repository.getDeviceHistoryList(deviceAddress)
            val avgRSSI = calculateAvgRSSI(deviceHistory)
            val distance = calculateDistance(avgRSSI)
            val stdDev = calculateStdDev(deviceHistory)
            val variance = calculateVariance(deviceHistory)
            val median = calculateMedian(deviceHistory)
            val range = calculateRange(deviceHistory)
            val min = calculteMin(deviceHistory)
            val max = calculteMax(deviceHistory)
            val mode = calculateMode(deviceHistory)

            repository.updateDeviceEntityDistance(deviceAddress, distance)

            repository.insertRssiStat(
                DeviceRssiStats(
                    deviceAddress = deviceAddress,
                    avgRssi = avgRSSI,
                    stdDev = stdDev,
                    variance = variance,
                    median = median,
                    range = range,
                    min = min,
                    max = max,
                    mode = mode,
                    count = deviceHistory.size
                )
            )
        }
    }

    fun calculateDistance(rssi: Int): Int {
        return 10.0.pow((baseRssi - rssi) / (10 * attenValue)).toInt()
    }

    fun calculateAvgRSSI(deviceHistory: List<DeviceRssiHistory>): Int {
        return deviceHistory.map { it.rssi }
            .average()
            .let { if (it.isNaN()) 0.0 else it }
            .toInt()
    }

    fun calculateStdDev(deviceHistory: List<DeviceRssiHistory>): Double {
        val avg = calculateAvgRSSI(deviceHistory)
        val variance = deviceHistory.map { (it.rssi - avg).toDouble().pow(2.0) }
            .average()
            .let { if (it.isNaN()) 0.0 else it }
        return kotlin.math.sqrt(variance)
    }

    fun calculateVariance(deviceHistory: List<DeviceRssiHistory>): Double {
        val avg = calculateAvgRSSI(deviceHistory)
        val variance = deviceHistory.map { (it.rssi - avg).toDouble().pow(2.0) }
            .average()
            .let { if (it.isNaN()) 0.0 else it }
        return variance
    }

    fun calculateMedian(deviceHistory: List<DeviceRssiHistory>): Int {
        val sortedList = deviceHistory.map { it.rssi }.sorted()
        val middle = sortedList.size / 2
        return if (sortedList.size % 2 == 0) {
            (sortedList[middle] + sortedList[middle - 1]) / 2
        } else {
            sortedList[middle]
        }
    }

    fun calculateRange(deviceHistory: List<DeviceRssiHistory>): Int {
        val sortedList = deviceHistory.map { it.rssi }.sorted()
        return sortedList.last() - sortedList.first()
    }

    fun calculteMin(deviceHistory: List<DeviceRssiHistory>): Int {
        return deviceHistory.map { it.rssi }.minOrNull() ?: 0
    }

    fun calculteMax(deviceHistory: List<DeviceRssiHistory>): Int {
        return deviceHistory.map { it.rssi }.maxOrNull() ?: 0
    }

    fun calculateMode(deviceHistory: List<DeviceRssiHistory>): Int {
        val modeMap = mutableMapOf<Int, Int>()
        deviceHistory.map { it.rssi }.forEach {
            modeMap[it] = modeMap.getOrDefault(it, 0) + 1
        }
        return modeMap.maxByOrNull { it.value }?.key ?: 0
    }
}