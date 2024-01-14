package com.corp.luqman.movielisting.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_movie")
data class Favorite(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Long = 0,
    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    var popularity: Double? = 0.0,
    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    var voteCount: Int? = 0,
    @ColumnInfo(name = "video")
    @SerializedName("video")
    var video: Boolean? = false,
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    var posterPath: String? = "",
    @ColumnInfo(name = "adult")
    @SerializedName("adult")
    var adult: Boolean? = false,
    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    var backdropPath: String? = "",
    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    var originalLanguage: String? = "",
    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    var originalTitle: String? = "",
    @ColumnInfo(name = "genre_ids")
    @SerializedName("genre_ids")
    var genreIds: MutableList<Int>? = null,
    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String? = "",
    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    var voteAverage: Double? = 0.0,
    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    var overview: String? = "",
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    var releaseDate: String? = ""
)

object ConverterListInt {

    @TypeConverter
    fun fromListIntToString(intList: MutableList<Int>): String = intList.toString()

    @TypeConverter
    fun toListIntFromString(stringList: String): MutableList<Int> {
        val result = ArrayList<Int>()
        val split = stringList.replace("[", "").replace("]", "").replace(" ", "").split(",")
        for (n in split) {
            try {
                result.add(n.toInt())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }
}
