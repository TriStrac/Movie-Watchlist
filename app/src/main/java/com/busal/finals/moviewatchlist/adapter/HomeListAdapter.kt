package com.busal.finals.moviewatchlist.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busal.finals.moviewatchlist.MovieDetailsViewActivity
import com.busal.finals.moviewatchlist.databinding.ActivityHomeListAdapterBinding
import com.busal.finals.moviewatchlist.models.MovieDetails
import com.busal.finals.moviewatchlist.storage.LocalStorage
import com.squareup.picasso.Picasso


class HomeListAdapter(
    private val activity:Activity,
    var movieLists:List<MovieDetails>,
    private val listReloadListener: ListReloadListener
): RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder>(){
    interface ListReloadListener {
        fun onListReloaded(source: String)
    }
    class HomeListViewHolder(
        private val activity: Activity,
        private val binding: ActivityHomeListAdapterBinding,
        private val adapter: HomeListAdapter,
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val selectedMovie = adapter.movieLists[position]
            val updateMovieList = LocalStorage(activity).movieList.toMutableList()
            val posterURL = selectedMovie.poster
            val index = updateMovieList.indexOfFirst { it.id == selectedMovie.id }
            if(index != -1){
                updateMovieList[index]=selectedMovie
            }

            Picasso.get().load(posterURL).into(binding.moviePoster)
            binding.movieTitleText.text = selectedMovie.name
            binding.yearText.text = selectedMovie.releaseDate
            binding.genreText.text = selectedMovie.genre
            binding.watchedButton.setOnClickListener {
                selectedMovie.isWatched = true
                LocalStorage(activity).movieList=updateMovieList
                adapter.reloadList("HomeListAdapter")
            }
            binding.removeButton.setOnClickListener {
                selectedMovie.isRemoved = true
                LocalStorage(activity).movieList=updateMovieList
                adapter.reloadList("HomeListAdapter")
            }
            binding.item.setOnClickListener{
                val intent = Intent(activity,MovieDetailsViewActivity::class.java)
                intent.putExtra("PARAM_ID",selectedMovie.id)
                activity.startActivity(intent)
            }
        }

    }

    private fun reloadList(source:String){
        listReloadListener.onListReloaded(source)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityHomeListAdapterBinding.inflate(
            inflater,
            parent,
            false,
        )
        return HomeListViewHolder(activity, binding,this)
    }

    override fun getItemCount() = movieLists.size

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        holder.bind(position)
    }
}