package com.example.spherelink.ui.add_device

sealed class AddDeviceEvent {
    data class OnDeviceAddressChange(val deviceAddress: String): AddDeviceEvent()
    object OnSaveTodoClick: AddDeviceEvent()
    object OnQrCodeScanned: AddDeviceEvent()
}