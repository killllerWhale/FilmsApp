package com.example.filmsapp.fragments.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.filmsapp.dataBase.MainDb
import com.example.filmsapp.dataBase.MovieItemDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class FavoriteVM : ViewModel() {

    private lateinit var db: MainDb

    val allMoviesFlow: Flow<List<MovieItemDb>> by lazy {
        db.getDao().getAllMovie().flowOn(Dispatchers.IO)
    }

    fun createDb(context: Context) {
        db = MainDb.getDb(context)
    }
}