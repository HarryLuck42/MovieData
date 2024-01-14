package com.corp.luqman.movielisting.utils

object Const {
    init {
        System.loadLibrary("native-lib")
    }

    private val environmentStage = 1
    private external fun appUrl(environmentStage: Int): String
    private external fun imageUrl(environmentStage: Int): String

    val appUrl = appUrl(environmentStage)
    val imageUrlbase = imageUrl(environmentStage)
    val apikey = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjMTUwZDZlZDA3NjFjNDIwMmZiNzM5MDcwZTM0ODZjNyIsInN1YiI6IjVmNWIyMjJjMmI5MzIwMDAzOGRiOTQ5MCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.OylJAP_vmpfQZnL91UnQoAADxxQMiOrTolYujsGYgqc"
    val language = "en-US"
    val POPULAR_PATH = "popular"
    val NOW_PLAYING_PATH = "now_playing"
    val UPCOMING_PATH = "upcoming"
}