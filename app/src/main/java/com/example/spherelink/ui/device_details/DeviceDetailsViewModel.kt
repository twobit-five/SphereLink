package com.example.spherelink.ui.device_details


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.entities.RssiValue
import com.example.spherelink.data.repository.DeviceRepository
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

    private val _history: Flow<List<RssiValue>> = if (address.isNotEmpty()) {
        repository.getDeviceHistory(address)
    } else {
        emptyFlow()
    }

    val history = MutableStateFlow<List<RssiValue>>(emptyList())

    var device by mutableStateOf<DeviceEntity?>(null)
        private set

    init {
        if(address !=  "") {
            viewModelScope.launch {
                _history.collect { historyData ->
                    history.value = historyData
                }
            }
            viewModelScope.launch(Dispatchers.IO) {
                val deviceEntity = repository.getDeviceByAddress(address)
                withContext(Dispatchers.Main) {
                    device = deviceEntity
                }
            }
        }
    }
}