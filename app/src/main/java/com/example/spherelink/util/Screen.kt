package com.example.spherelink.util

const val DEVICE_DETAIL_ARGUMENT_KEY = "deviceAddress"

sealed class Screen(val route: String) {
    object DeviceList : Screen(route = "device_list")
    object AddDevice : Screen(route = "add_device")
    object DeviceDetails : Screen(route = "device_details/{$DEVICE_DETAIL_ARGUMENT_KEY}") {
        fun passAddress(deviceAddress: String): String {
            return "device_details/$deviceAddress"
        }
    }
}
