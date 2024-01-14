package com.corp.luqman.movielisting.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.corp.luqman.movielisting.data.models.ConverterListInt
import com.corp.luqman.movielisting.data.models.Favorite

@TypeConverters(ConverterListInt::class)
@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase(){
    abstract fun movieDao(): MovieDao
}