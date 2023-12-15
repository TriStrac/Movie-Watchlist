package com.busal.finals.moviewatchlist.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busal.finals.moviewatchlist.MovieDetailsViewActivity
import com.busal.finals.moviewatchlist.databinding.ActivitySearchListAdapterBinding
import com.busal.finals.moviewatchlist.models.MovieDetails
import com.busal.finals.moviewatchlist.storage.LocalStorage
import com.squareup.picasso.Picasso


class SearchListAdapter(
    private val activity: Activity,
    var searchedMovieLists:List<MovieDetails>,
    private val listReloadListener: ListReloadListener
): RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder>(){
    interface ListReloadListener {
        fun onListReloaded(source: String)
    }
    class SearchListViewHolder(
        private val activity: Activity,
        private val binding: ActivitySearchListAdapterBinding,
        private val adapter: SearchListAdapter,
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val selectedMovie = adapter.searchedMovieLists[position]
            val posterURL = selectedMovie.poster
            Picasso.get().load(posterURL).into(binding.searchedMoviePoster)
            binding.searchedMovieTitleText.text = selectedMovie.name
            binding.searchedYearText.text = selectedMovie.releaseDate
            binding.searchedGenreText.text = selectedMovie.genre

            binding.searchedAddToWatchListButton.setOnClickListener {
                val movieList = LocalStorage(activity).movieList.toMutableList()
                movieList.add(0,selectedMovie)
                LocalStorage(activity).movieList = movieList
                adapter.reloadList("SearchListAdapter")
            }

            binding.item.setOnClickListener{
                val intent = Intent(activity, MovieDetailsViewActivity::class.java)
                intent.putExtra("PARAM_ID",selectedMovie.id)
                intent.putExtra("PARAM_ADDORCHECK",true)
                activity.startActivity(intent)
            }
        }
    }
    private fun reloadList(source:String){
        listReloadListener.onListReloaded(source)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivitySearchListAdapterBinding.inflate(
            inflater,
            parent,
            false,
        )
        return SearchListViewHolder(activity, binding, this)
    }

    override fun getItemCount() = searchedMovieLists.size

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        holder.bind(position)
    }
}