package com.example.spherelink.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.spherelink.data.database.AppDatabase
import com.example.spherelink.data.repository.DeviceRepository
import com.example.spherelink.data.repository.DeviceRepositoryImpl
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
}