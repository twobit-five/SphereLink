package com.example.spherelink.ui.device_list

import com.example.spherelink.data.entities.DeviceEntity

sealed class DeviceListEvent {
    data class OnDeleteDeviceClick(val device: DeviceEntity): DeviceListEvent()
    data class OnDoneChange(val device: DeviceEntity, val isDone: Boolean): DeviceListEvent()
    object OnUndoDeleteClick: DeviceListEvent()
    data class OnDeviceClick(val device: DeviceEntity): DeviceListEvent()
    object OnAddDeviceClick: DeviceListEvent()
}


