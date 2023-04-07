package com.example.spherelink.ui.device_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.repository.DeviceRepository
import com.example.spherelink.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceDetailsViewModel @Inject constructor(
    private val repository: DeviceRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var device by mutableStateOf<DeviceEntity?>(null)
        private set

    var deviceAddress by mutableStateOf("")
        private set

    var deviceName by mutableStateOf("")
        private set

    var deviceRssi by mutableStateOf("")
        private set

    var deviceDistance by mutableStateOf("")
        private set

    var deviceTimestamp by mutableStateOf("")
        private set

    var deviceIsConnected by mutableStateOf("")
        private set

    var deviceBatteryLevel by mutableStateOf("")
        private set

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val deviceId = savedStateHandle.get<Int>("deviceId")!!
        if(deviceId !=  -1) {
            viewModelScope.launch {
                repository.getDeviceById(deviceId).let { deviceEntity ->
                    deviceAddress = deviceEntity.address
                    deviceName = deviceEntity.device_name
                    deviceRssi = deviceEntity.rssi.toString()
                    deviceDistance = deviceEntity.distance.toString()
                    deviceTimestamp = deviceEntity.timestamp.toString()
                    deviceIsConnected = deviceEntity.isConnected.toString()
                    deviceBatteryLevel = deviceEntity.batteryLevel.toString()

                    this@DeviceDetailsViewModel.device = device
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}