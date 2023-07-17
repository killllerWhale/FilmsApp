package com.example.filmsapp.di

import com.example.filmsapp.dataPrefs.Prefs
import com.example.filmsapp.fragments.movie.MovieVM
import com.example.filmsapp.fragments.movies.MoviesVM
import com.example.filmsapp.fragments.setting.SettingVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    single { Prefs.getInstance(get()) }
    viewModel { MoviesVM() }
    viewModel { MovieVM() }
    viewModelOf(::SettingVM)
}