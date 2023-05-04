package com.example.spherelink.di

import com.example.spherelink.data.repository.BarcodeScannerRepo
import com.example.spherelink.domain.repo.BarcodeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindBarcodeRepository(impl: BarcodeScannerRepo): BarcodeRepository
}