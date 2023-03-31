package com.example.spherelink.domain.distance

interface DistanceCalculator {
    fun calculateDistance(rssi: Int): Int
}