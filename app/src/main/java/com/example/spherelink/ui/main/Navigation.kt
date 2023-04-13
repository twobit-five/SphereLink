package com.example.spherelink.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spherelink.ui.add_edit_device.AddDeviceScreen
import com.example.spherelink.ui.device_list.DeviceListScreen
import com.example.spherelink.util.Routes

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.DEVICE_LIST
    ) {
        composable(Routes.DEVICE_LIST) {
            DeviceListScreen(
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
        composable(
            route = Routes.ADD_EDIT_DEVICE + "?deviceAddress={deviceAddress}",
            arguments = listOf(
                navArgument(name = "deviceAddress") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            AddDeviceScreen(onPopBackStack = {
                navController.popBackStack()
            })
        }
    }
}