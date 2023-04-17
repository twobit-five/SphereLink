package com.example.spherelink.ui.add_device

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.domain.repo.BarcodeRepository
import com.example.spherelink.domain.repo.DeviceRepository
import com.example.spherelink.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDeviceViewModel @Inject constructor(
    private val repository: DeviceRepository,
    private val barcodeRepo : BarcodeRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var device by mutableStateOf<DeviceEntity?>(null)
        private set

    var deviceAddress by mutableStateOf("")
        private set

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val address = savedStateHandle.get<String>("deviceAddress") ?: ""
        if(address !=  "") {
            viewModelScope.launch {
                repository.getDeviceByAddress(address).let { deviceEntity ->
                    //deviceAddress = deviceEntity.address
                    this@AddDeviceViewModel.device = device
                }
            }
        }
    }

    fun onEvent(event: AddDeviceEvent) {
        when(event) {
            is AddDeviceEvent.OnDeviceAddressChange -> {
                deviceAddress = event.deviceAddress
            }
            is AddDeviceEvent.OnSaveTodoClick -> {
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
                            isDone = device?.isDone ?: false,
                            isConnected = false,
                            batteryLevel = 0
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
            is AddDeviceEvent.OnQrCodeScanned -> {
                startScanning()
            }
        }
    }


    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    fun startScanning(){
        viewModelScope.launch {
            barcodeRepo.startScanning().collect{
                if (!it.isNullOrBlank()){
                    deviceAddress = it
                }
            }
        }
    }
}

