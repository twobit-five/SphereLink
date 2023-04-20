package com.example.spherelink.domain.distance

import com.example.spherelink.data.entities.DeviceEntity

interface DistanceCalculator {
    fun updateDistance(deviceAddress: String, currentRSSI: Int)
}