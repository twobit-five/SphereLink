package com.example.spherelink.domain.repo

import kotlinx.coroutines.flow.Flow

interface BarcodeRepository {
    fun startScanning(): Flow<String?>
}