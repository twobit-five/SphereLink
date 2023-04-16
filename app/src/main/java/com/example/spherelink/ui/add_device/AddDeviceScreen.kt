package com.example.spherelink.ui.add_device

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spherelink.util.UiEvent


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddDeviceScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddDeviceViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    //TODO need to add logic to handle the camera button

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }
                else -> Unit
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        floatingActionButton = {
            Box {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(AddDeviceEvent.OnSaveTodoClick)
                    },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save"
                    )
                }
                FloatingActionButton(
                    onClick = {
                        // Handle camera button click here
                        viewModel.onEvent(AddDeviceEvent.OnQrCodeScanned)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(y = (-56 - 16).dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Camera"
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                value = viewModel.deviceAddress,
                onValueChange = {
                    viewModel.onEvent(AddDeviceEvent.OnDeviceAddressChange(it))
                },
                placeholder = {
                    Text(text = "Device Address")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

