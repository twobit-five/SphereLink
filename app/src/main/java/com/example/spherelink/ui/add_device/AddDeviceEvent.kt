package com.example.spherelink.ui.add_device

sealed class AddDeviceEvent {
    data class OnDeviceAddressChange(val deviceAddress: String): AddDeviceEvent()
    //data class OnDeviceNameChange(val deviceName: String): AddEditDeviceEvent()
    object OnSaveTodoClick: AddDeviceEvent()

    object OnQrCodeScanned: AddDeviceEvent()

}