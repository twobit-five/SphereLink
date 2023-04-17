package com.example.spherelink.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.spherelink.data.database.AppDatabase
import com.example.spherelink.domain.repo.DeviceRepository
import com.example.spherelink.data.repository.DeviceRepositoryImpl
import com.example.spherelink.domain.bluetooth.DeviceManager
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
    fun provideAppDatabase(app: Application) : AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "app_database",
        ).build()
    }

    @Provides
    @Singleton
    fun providesDeviceRepository(db: AppDatabase): DeviceRepository {
        return DeviceRepositoryImpl(db.deviceDao())
    }
    @Provides
    @Singleton
    fun providesDeviceManager(
        context: Context,
        repository: DeviceRepository
    ): DeviceManager {
        return DeviceManager(context, repository)
    }
}