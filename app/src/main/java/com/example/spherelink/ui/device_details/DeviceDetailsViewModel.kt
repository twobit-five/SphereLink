package com.example.spherelink.ui.device_details


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.entities.DeviceRssiHistory
import com.example.spherelink.data.entities.DeviceRssiStats
import com.example.spherelink.domain.repo.DeviceRepository
import com.example.spherelink.util.DEVICE_DETAIL_ARGUMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DeviceDetailsViewModel @Inject constructor(
    private val repository: DeviceRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val address = savedStateHandle.get<String>(DEVICE_DETAIL_ARGUMENT_KEY) ?: ""

    private val _history: Flow<List<DeviceRssiHistory>> = if (address.isNotEmpty()) {
        repository.getDeviceHistory(address)
    } else {
        emptyFlow()
    }

    val history = MutableStateFlow<List<DeviceRssiHistory>>(emptyList())

    private val _device: Flow<DeviceEntity> = if (address.isNotEmpty()) {
        repository.getDeviceEntityByAddress(address)
    } else {
        emptyFlow()
    }
    var device = MutableStateFlow<DeviceEntity?>(null)

    private val _stats: Flow<DeviceRssiStats> = if (address.isNotEmpty()) {
        repository.getRssiStats(address)
    } else {
        emptyFlow()
    }

    var stats = MutableStateFlow<DeviceRssiStats?>(null)

    init {
        if(address !=  "") {
            viewModelScope.launch {
                _history.collect { historyData ->
                    history.value = historyData
                }
            }
            viewModelScope.launch {
                _device.collect { deviceData ->
                    device.value = deviceData
                }
            }
            viewModelScope.launch {
                _stats.collect { statsData ->
                    stats.value = statsData
                }
            }
        }
    }
}