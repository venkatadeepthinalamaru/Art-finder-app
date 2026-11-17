package com.example.artfinder.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArtworkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ArtworkEntity)

    @Query("SELECT * FROM favourites ORDER BY title")
    suspend fun getAll(): List<ArtworkEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favourites WHERE id = :id)")
    suspend fun isSaved(id: Int): Boolean

    @Query("DELETE FROM favourites WHERE id = :id")
    suspend fun delete(id: Int)
}
