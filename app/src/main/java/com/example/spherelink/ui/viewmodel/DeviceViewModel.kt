package com.example.spherelink.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.repository.DeviceRepository
import kotlinx.coroutines.launch

class DeviceViewModel(private val repository: DeviceRepository) : ViewModel() {

    val allDevices: LiveData<List<DeviceEntity>> = repository.allDevices


    fun deleteDevice(address: String) = viewModelScope.launch {
        repository.deleteDevice(address)
    }
}
