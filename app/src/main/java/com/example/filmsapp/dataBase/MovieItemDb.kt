package com.example.filmsapp.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "movies")
data class MovieItemDb (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "poster_path")
    val poster_path: String,
    @ColumnInfo(name = "release_date")
    val release_date: String,
    @ColumnInfo(name = "production_countries")
    val production_countries: String,
    @ColumnInfo(name = "genres")
    val genres: String,
    @ColumnInfo(name = "production_companies")
    val production_companies: String,
    @ColumnInfo(name = "tagline")
    val tagline: String
)