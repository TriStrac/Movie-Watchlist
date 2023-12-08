package com.busal.finals.moviewatchlist.adapter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.busal.finals.moviewatchlist.MovieDetailsViewActivity
import com.busal.finals.moviewatchlist.databinding.ActivityWatchedListAdapterBinding
import com.busal.finals.moviewatchlist.models.MovieDetails
import com.busal.finals.moviewatchlist.storage.LocalStorage

class WatchedListAdapter(
    private val activity: Activity,
    private var movieList:List<MovieDetails>,
): RecyclerView.Adapter<WatchedListAdapter.WatchedListViewHolder>(){

    class WatchedListViewHolder(
        private val activity: Activity,
        private val binding: ActivityWatchedListAdapterBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(movieDetails: MovieDetails){
            binding.movieTitleText2.text = movieDetails.name
            binding.yearText2.text = movieDetails.releaseDate
            binding.genreText2.text = movieDetails.genre
            if(movieDetails.isWatched){
                binding.statusText2.text = "Watched"
            }
            if(movieDetails.isRemoved){
                binding.statusText2.text = "Removed"
            }

            binding.item2.setOnClickListener{
                val intent = Intent(activity, MovieDetailsViewActivity::class.java)
                intent.putExtra("PARAM_ID",movieDetails.id)
                activity.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchedListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityWatchedListAdapterBinding.inflate(
            inflater,
            parent,
            false,
        )
        return WatchedListViewHolder(activity, binding)
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: WatchedListViewHolder, position: Int) {
        holder.bind(movieList[position])
    }
}