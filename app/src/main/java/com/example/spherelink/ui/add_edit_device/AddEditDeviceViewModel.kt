package com.example.spherelink.ui.add_edit_device

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
class AddEditDeviceViewModel @Inject constructor(
    private val repository: DeviceRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var device by mutableStateOf<DeviceEntity?>(null)
        private set

    var deviceAddress by mutableStateOf("")
        private set

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        var address = savedStateHandle.get<String>("deviceAddress") ?: ""
        if(address !=  "") {
            viewModelScope.launch {
                repository.getDeviceByAddress(address).let { deviceEntity ->
                    deviceAddress = deviceEntity.address
                    this@AddEditDeviceViewModel.device = device
                }
            }
        }
    }

    fun onEvent(event: AddEditDeviceEvent) {
        when(event) {
            is AddEditDeviceEvent.OnDeviceAddressChange -> {
                deviceAddress = event.deviceAddress
            }
            //is AddEditDeviceEvent.OnDeviceNameChange -> {
            //    deviceName = event.deviceName
            //}
            is AddEditDeviceEvent.OnSaveTodoClick -> {
                viewModelScope.launch {
                    if(deviceAddress.isBlank()) {
                        sendUiEvent(UiEvent.ShowSnackbar(
                            message = "The Device Address can't be empty"
                        ))
                        return@launch
                    }
                    repository.insertDevice(
                        DeviceEntity(
                            address = deviceAddress,
                            device_name = "",
                            rssi = 0,
                            distance = 0,
                            timestamp = 0,
                            isDone = device?.isDone ?: false
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
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