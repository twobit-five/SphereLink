package com.example.spherelink.ui.add_edit_device

sealed class AddEditDeviceEvent {
    data class OnDeviceAddressChange(val deviceAddress: String): AddEditDeviceEvent()
    //data class OnDeviceNameChange(val deviceName: String): AddEditDeviceEvent()
    object OnSaveTodoClick: AddEditDeviceEvent()
}