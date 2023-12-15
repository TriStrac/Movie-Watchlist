package com.busal.finals.moviewatchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.busal.finals.moviewatchlist.databinding.ActivityMovieDetailsViewBinding
import com.busal.finals.moviewatchlist.models.MovieDetails
import com.busal.finals.moviewatchlist.storage.LocalStorage
import com.squareup.picasso.Picasso

class MovieDetailsViewActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMovieDetailsViewBinding
    private var isAddOrCheck : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMovieDetailsViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val movieId =intent.getIntExtra("PARAM_ID",-1)
        isAddOrCheck = intent.getBooleanExtra("PARAM_ADDORCHECK",false)
        val movie = getMovieDetails(movieId)
        if (movie!=null){
            val posterURL = movie.poster
            Picasso.get().load(posterURL).into(binding.moviePosterView)
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
        val movieList : List<MovieDetails>
        if(isAddOrCheck){
            movieList = LocalStorage(this).searchedMovieList
        }else{
            movieList = LocalStorage(this).movieList
        }

        return movieList.find{it.id==id}
    }
}