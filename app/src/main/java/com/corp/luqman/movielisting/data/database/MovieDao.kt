package com.corp.luqman.movielisting.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.corp.luqman.movielisting.data.models.Favorite

@Dao
interface MovieDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAllGenre(genres: MutableList<GenreData>)
//
//    @Query("SELECT * FROM data_genre")
//    fun getAll(): LiveData<MutableList<GenreData>>?

    @Query("SELECT * from favorite_movie")
    suspend fun getFavorites(): MutableList<Favorite>?

    @Query("SELECT * from favorite_movie where title like :title")
    suspend fun searchFavorite(title: String): MutableList<Favorite>??

    @Query("SELECT * from favorite_movie where id =:id")
    suspend fun getFavById(id: String): Favorite

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Query("DELETE from favorite_movie")
    suspend fun deleteAllFavorites()

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

}