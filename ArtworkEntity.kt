package com.example.artfinder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class ArtworkEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val artist: String,
    val date: String,
    val imageId: String
)
