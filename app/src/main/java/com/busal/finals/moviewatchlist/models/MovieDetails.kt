package com.busal.finals.moviewatchlist.models

data class MovieDetails (
    val poster: String,
    val type: String,
    val name: String,
    val releaseDate: String,
    val genre: String,
    val credential: String,
    val duration: String,
    val director: String,
    val synopsis: String,
    val userRating: String,
    val isWatched: Boolean,
    val isRemoved: Boolean,
)