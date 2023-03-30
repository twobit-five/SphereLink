package com.example.spherelink.ui.add_device

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDeviceViewModel @Inject constructor(
    private val repository: DeviceRepository,
    saveStateHandle: SavedStateHandle
): ViewModel() {

    var device = mutableStateOf<DeviceEntity?>(null)
        private set

    var deviceAddress = mutableStateOf("")
        private set

    init{
        viewModelScope.launch {
        }
    }
}