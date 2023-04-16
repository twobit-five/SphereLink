package com.example.spherelink.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spherelink.ui.add_device.AddDeviceScreen
import com.example.spherelink.ui.device_details.DeviceDetailsScreen
import com.example.spherelink.ui.device_list.DeviceListScreen
import com.example.spherelink.util.DEVICE_DETAIL_ARGUMENT_KEY
import com.example.spherelink.util.Routes
import com.example.spherelink.util.Screen

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.DeviceList.route
    ) {
        composable(
            route = Screen.DeviceList.route
        ) {
            DeviceListScreen(
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
        composable(
            route = Screen.AddDevice.route,
        ) {
            AddDeviceScreen(onPopBackStack = {
                navController.popBackStack()
            })
        }
        composable(
            route = Screen.DeviceDetails.route,
            arguments = listOf(
                navArgument(name = DEVICE_DETAIL_ARGUMENT_KEY) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            DeviceDetailsScreen(
                onPopBackStack = {
                    navController.popBackStack()
                }
            )
        }
    }
}