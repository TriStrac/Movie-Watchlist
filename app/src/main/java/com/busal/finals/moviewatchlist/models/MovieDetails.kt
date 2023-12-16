package com.busal.finals.moviewatchlist.models

data class MovieDetails (
    val id: Int,
    val poster: String,
    val type: String,
    val name: String,
    val releaseDate: String,
    val genre: String,
    val credential: String,
    val duration: String,
    val director: String,
    val synopsis: String,
    var userRating: String,
    var isWatched: Boolean,
    var isRemoved: Boolean,
)