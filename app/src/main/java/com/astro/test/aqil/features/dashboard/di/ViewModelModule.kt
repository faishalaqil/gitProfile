package com.astro.test.aqil.features.dashboard.di

import com.astro.test.aqil.features.dashboard.viewmodel.ProfilesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{
        ProfilesViewModel()
    }
}