package com.example.spherelink.domain.distance

import com.example.spherelink.data.entities.DeviceEntity

interface DistanceCalculator {
    fun calculateDistance(deviceAddress: String, currentRSSI: Int)
}