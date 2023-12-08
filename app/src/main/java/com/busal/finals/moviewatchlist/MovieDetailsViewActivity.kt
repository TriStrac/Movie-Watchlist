package com.busal.finals.moviewatchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.busal.finals.moviewatchlist.databinding.ActivityMovieDetailsViewBinding
import com.busal.finals.moviewatchlist.models.MovieDetails
import com.busal.finals.moviewatchlist.storage.LocalStorage

class MovieDetailsViewActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMovieDetailsViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMovieDetailsViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val movieId =intent.getIntExtra("PARAM_ID",-1)
        val movie = getMovieDetails(movieId)
        if (movie!=null){
            binding.typeText.text = movie.type
            binding.titleText.text = movie.name
            binding.airedText.text = movie.releaseDate
            binding.genresText.text = movie.genre
            binding.ratingText.text = movie.credential
            binding.durationText.text = movie.duration
            binding.directorText.text = movie.director
            binding.synopsisText.text = movie.synopsis
        }
    }
    private fun getMovieDetails(id:Int): MovieDetails?{
        val movieList = LocalStorage(this).movieList
        return movieList.find{it.id==id}
    }
}