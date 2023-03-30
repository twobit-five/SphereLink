package com.example.spherelink

import android.annotation.SuppressLint

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.ui.device_list.DeviceCard
import com.example.spherelink.ui.theme.SphereLinkTheme
import dagger.hilt.android.AndroidEntryPoint

val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SphereLinkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}


