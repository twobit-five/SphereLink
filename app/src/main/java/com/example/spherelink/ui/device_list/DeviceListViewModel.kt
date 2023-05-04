package com.example.spherelink.ui.device_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spherelink.util.UiEvent
import com.example.spherelink.domain.repo.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.util.Screen

@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val repository: DeviceRepository
): ViewModel() {

    val devices = repository.getAllDeviceEntities()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedDevice: DeviceEntity? = null

    fun onEvent(event: DeviceListEvent) {
        when (event) {
            is DeviceListEvent.OnAddDeviceClick -> {
                sendUiEvent(UiEvent.Navigate(Screen.AddDevice.route))
            }
            is DeviceListEvent.OnDeviceClick -> {
                //sendUiEvent(UiEvent.Navigate(Routes.DeviceDetails + "/${event.device.id}"))
                //this is where we need to send the device id to the device details screen
                Log.v("DeviceListViewModel", "Device clicked: ${event.device.address}")
                Log.v("DeviceListViewModel", "Device arguments: ${Screen.DeviceDetails.passAddress(event.device.address)}")
                sendUiEvent(UiEvent.Navigate(Screen.DeviceDetails.passAddress(event.device.address)))
            }
            is DeviceListEvent.OnDeleteDeviceClick -> {
                viewModelScope.launch {
                    deletedDevice = event.device
                    repository.deleteDeviceEntity(event.device)

                    sendUiEvent(UiEvent.ShowSnackbar(
                        message = "Device deleted",
                        action = "Undo"
                    ))
                }

            }
            is DeviceListEvent.OnUndoDeleteClick -> {
                deletedDevice?.let { device ->
                    viewModelScope.launch {
                        repository.insertDeviceEntity(device)
                    }
                }
            }
            is DeviceListEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertDeviceEntity(
                        event.device.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}