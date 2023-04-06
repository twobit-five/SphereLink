package com.example.spherelink.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.spherelink.data.database.RssiDatabase
import com.example.spherelink.data.repository.DeviceRepository
import com.example.spherelink.data.repository.DeviceRepositoryImpl
import com.example.spherelink.domain.distance.DistanceCalculator
import com.example.spherelink.domain.distance.DistanceCalculatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application) : RssiDatabase {
        return Room.databaseBuilder(
            app,
            RssiDatabase::class.java,
            "rssi_database",
        ).build()
    }

    @Provides
    @Singleton
    fun providesDeviceRepository(db: RssiDatabase): DeviceRepository {
        return DeviceRepositoryImpl(db.deviceDao())
    }
}