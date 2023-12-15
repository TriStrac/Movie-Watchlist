package com.busal.finals.moviewatchlist.storage

import android.content.Context
import com.busal.finals.moviewatchlist.models.MovieDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalStorage(context: Context) {
    private val sharedPref = context
        .getSharedPreferences("GENERAL", Context.MODE_PRIVATE)

    var isSaved: Boolean
        get() = sharedPref.getBoolean("KEY_ISSAVED",true)
        set(value) = sharedPref.edit().putBoolean("KEY_ISSAVED",value).apply()

    private inline fun <reified T> Gson.toJson(src: T): String {
        return toJson(src, object : TypeToken<T>() {}.type)
    }

    private inline fun <reified T> Gson.fromJson(json: String): T {
        return fromJson(json, object : TypeToken<T>() {}.type)
    }

    var movieList: List<MovieDetails>
        get() {
            val json: String? = sharedPref.getString("KEY_MOVIE_LIST", null)
            return if (json != null) {
                Gson().fromJson(json)
            } else {
                emptyList()
            }
        }
        set(value) {
            val gson = Gson()
            val json = gson.toJson(value)
            sharedPref.edit().putString("KEY_MOVIE_LIST", json).apply()
        }

    var searchedMovieList: List<MovieDetails>
        get() {
            val json: String? = sharedPref.getString("KEY_SEARCHED_MOVIE_LIST", null)
            return if (json != null) {
                Gson().fromJson(json)
            } else {
                emptyList()
            }
        }
        set(value) {
            val gson = Gson()
            val json = gson.toJson(value)
            sharedPref.edit().putString("KEY_SEARCHED_MOVIE_LIST", json).apply()
        }
}