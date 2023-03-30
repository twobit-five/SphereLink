package com.example.spherelink.ui.device_list

import com.example.spherelink.data.entities.DeviceEntity

sealed class DeviceListEvent {

    data class onDeleteDeviceClick(val device: DeviceEntity): DeviceListEvent()
    object OnUndoDeleteClick: DeviceListEvent()
    object OnAddDeviceClick: DeviceListEvent()
}


