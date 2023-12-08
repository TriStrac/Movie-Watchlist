package com.busal.finals.moviewatchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.busal.finals.moviewatchlist.adapter.HomeListAdapter
import com.busal.finals.moviewatchlist.adapter.WatchedListAdapter
import com.busal.finals.moviewatchlist.databinding.FragmentWatchedBinding
import com.busal.finals.moviewatchlist.models.MovieDetails
import com.busal.finals.moviewatchlist.storage.LocalStorage


class WatchedFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private lateinit var binding:FragmentWatchedBinding
    private lateinit var movieList:List<MovieDetails>
    private var onWatched = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchedBinding.inflate(inflater,container,false)
        getMovieList()
        showWatched()
        binding.watchedOrRemovedButton.setOnClickListener {
            if(onWatched){
                binding.watchedOrRemovedButton.text="Removed"
                onWatched=false
                showRemoved()
            }else{
                binding.watchedOrRemovedButton.text="Watched"
                onWatched=true
                showWatched()
            }
        }
        return binding.root
    }
    private fun getMovieList(){
        movieList=LocalStorage(requireContext()).movieList
    }
    private fun displayMovies(data:List<MovieDetails>){
        binding.watchedListRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.watchedListRecyclerView.adapter = WatchedListAdapter(requireActivity(), data)
    }

    private lateinit var displayedList: List<MovieDetails>
    private fun showRemoved() {
        displayedList = movieList.filter { it.isRemoved && !it.isWatched }
        displayMovies(displayedList)
    }

    private fun showWatched() {
        displayedList = movieList.filter { it.isWatched && !it.isRemoved }
        displayMovies(displayedList)
    }

}