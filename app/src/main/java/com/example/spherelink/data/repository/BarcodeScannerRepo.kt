package com.example.spherelink.data.repository

import com.example.spherelink.domain.repo.BarcodeRepository
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BarcodeScannerRepo @Inject constructor(
    private val scanner: GmsBarcodeScanner,
): BarcodeRepository {

    override fun startScanning(): Flow<String?> {
        return callbackFlow {
            scanner.startScan()
                .addOnSuccessListener {
                    launch {
                        send(getDetails(it))
                    }
                }.addOnFailureListener {
                    it.printStackTrace()
                }
            awaitClose {  }
        }

    }


    private fun getDetails(barcode: Barcode): String {
        return when (barcode.valueType) {
            Barcode.TYPE_TEXT -> {
                "${barcode.rawValue}".trim { it <= ' ' }.toUpperCase()
            }
            Barcode.TYPE_UNKNOWN -> {
                "${barcode.rawValue}".trim { it <= ' ' }.toUpperCase()
            }
            else -> {
                "UNKNOWN"
            }
        }

    }
}