package com.busal.finals.moviewatchlist.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.busal.finals.moviewatchlist.HomeFragment
import com.busal.finals.moviewatchlist.MovieDetailsViewActivity
import com.busal.finals.moviewatchlist.databinding.ActivityHomeListAdapterBinding
import com.busal.finals.moviewatchlist.models.MovieDetails
import com.busal.finals.moviewatchlist.storage.LocalStorage


class HomeListAdapter(
    private val activity:Activity,
    var movieLists:List<MovieDetails>,
    private val listReloadListener: ListReloadListener
): RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder>(){
    interface ListReloadListener {
        fun onListReloaded()
    }
    class HomeListViewHolder(
        private val activity: Activity,
        private val binding: ActivityHomeListAdapterBinding,
        private val adapter: HomeListAdapter,
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val movieDetails = adapter.movieLists[position]
            binding.movieTitleText.text = movieDetails.name
            binding.yearText.text = movieDetails.releaseDate
            binding.genreText.text = movieDetails.genre
            binding.watchedButton.setOnClickListener {
                movieDetails.isWatched = true
                adapter.saveToLocal(position)
                adapter.reloadList()
            }
            binding.removeButton.setOnClickListener {
                movieDetails.isRemoved = true
                adapter.saveToLocal(position)
                adapter.reloadList()
            }
            binding.item.setOnClickListener{
                val intent = Intent(activity,MovieDetailsViewActivity::class.java)
                intent.putExtra("PARAM_ID",movieDetails.id)
                activity.startActivity(intent)
            }
        }

    }

    private fun saveToLocal(position: Int){
        LocalStorage(activity).movieList = movieLists
        notifyItemChanged(position)
    }
    private fun reloadList(){
        listReloadListener.onListReloaded()
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