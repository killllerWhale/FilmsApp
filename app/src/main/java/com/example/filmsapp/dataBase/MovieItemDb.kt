package com.example.filmsapp.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "movies")
data class MovieItemDb(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val overview: String,
    val title: String,
    val yearOfProduction: String,
    val poster_path: String,
    val production_countries: String,
    val genres: String,
    val production_companies: String,
    val tagline: String
)