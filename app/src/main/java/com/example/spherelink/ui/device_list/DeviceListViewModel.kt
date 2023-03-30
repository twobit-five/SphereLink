package com.example.spherelink.ui.device_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spherelink.util.UiEvent
import com.example.spherelink.data.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.example.spherelink.util.Routes
import com.example.spherelink.data.entities.DeviceEntity

@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val repository: DeviceRepository
): ViewModel() {

    val devices = repository.getAllDevices()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedDevice: DeviceEntity? = null

    fun onEvent(event: DeviceListEvent) {
        when (event) {
            is DeviceListEvent.OnAddDeviceClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_DEVICE))
            }
            is DeviceListEvent.onDeleteDeviceClick -> {
                viewModelScope.launch {
                    deletedDevice = event.device
                    repository.deleteDevice(event.device)

                    sendUiEvent(UiEvent.ShowSnackbar(
                        message = "Device deleted",
                        actionLabel = "Undo"
                    ))
                }

            }
            is DeviceListEvent.OnUndoDeleteClick -> {
                deletedDevice?.let { device ->
                    viewModelScope.launch {
                        repository.insertDevice(device)
                    }
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