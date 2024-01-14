package com.corp.luqman.movielisting.data.database

import androidx.room.*
import com.corp.luqman.movielisting.data.models.Favorite

@Dao
interface MovieDao {

    @Query("SELECT * from favorite_movie")
    suspend fun getFavorites(): MutableList<Favorite>?

    @Query("SELECT * from favorite_movie where title like :title")
    suspend fun searchFavorite(title: String): MutableList<Favorite>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

}