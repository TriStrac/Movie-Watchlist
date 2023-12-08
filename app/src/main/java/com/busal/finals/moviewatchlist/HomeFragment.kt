package com.busal.finals.moviewatchlist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.busal.finals.moviewatchlist.adapter.HomeListAdapter
import com.busal.finals.moviewatchlist.databinding.FragmentHomeBinding
import com.busal.finals.moviewatchlist.models.MovieDetails
import com.busal.finals.moviewatchlist.storage.LocalStorage

class HomeFragment : Fragment(), HomeListAdapter.ListReloadListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private lateinit var binding:FragmentHomeBinding
    private lateinit var movieList:List<MovieDetails>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.searchButton.setOnClickListener {


            binding.searchButton.visibility = View.INVISIBLE
            binding.addMovieButton.visibility = View.INVISIBLE
            binding.doneButton.visibility = View.VISIBLE
            binding.searchBarText.visibility = View.VISIBLE
        }

        binding.doneButton.setOnClickListener {
            binding.searchButton.visibility = View.VISIBLE
            binding.addMovieButton.visibility = View.VISIBLE
            binding.doneButton.visibility = View.INVISIBLE
            binding.searchBarText.visibility = View.INVISIBLE
        }

        initializeMovies()
        saveToLocal()
        displayMovies()
        return binding.root
    }
    override fun onListReloaded() {
        displayMovies()
    }
    private lateinit var listToDisplay:List<MovieDetails>
    private fun filterDisplay(){
        listToDisplay = LocalStorage(requireContext()).movieList
        listToDisplay = listToDisplay.filter { !it.isRemoved && !it.isWatched }
    }
    private fun displayMovies(){
        filterDisplay()
        binding.movieListRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.movieListRecyclerView.adapter = HomeListAdapter(requireActivity(), listToDisplay,this)
    }
    private fun saveToLocal(){
        LocalStorage(requireContext()).movieList=movieList
    }
    private fun initializeMovies(){
        movieList=listOf(
            MovieDetails(1,"poster link","Anime","Oshi no ko","2023","kPop","PG","12 episodes","YOASOBI","eren dead","5",false,false),
            MovieDetails(2,"poster link","Anime","kanojo mo kanojo","2032","kids","G","200","YOASOBI","eren sdfsgsdfsdfgdead","5",false,false),
            MovieDetails(3,"poster link","Anime","genshit impact","2212","kids","PG","12 episodes","YOASOBI","erensfggsfdsfdgsdfg dead","5",false,false),
            MovieDetails(4,"poster link","Anime","insidious","2122","horror","PG","12 episodes","YOASOBI","eren dsgdfsdfgsdfgsgdfead","5",false,false),
            MovieDetails(5,"poster link","Anime","hahahahaha","2444","kids","PG","200","YOASOBI","eren gsfdsgdfsdfgdead","5",false,false),
            MovieDetails(12,"poster link","Anime","Oshi no ko","2332","horror","G","200","YOASOBI","eren dead","5",false,false),
            MovieDetails(22,"poster link","Anime","kanojo mo kanojo","2014","kids","ALL","12 episodes","YOASOBI","sdfggsdfsdfgsdfgeren dead","5",false,false),
            MovieDetails(32,"poster link","Anime","genshit impact","2013","horror","18+","200","Hololive","sdfgsdfgsdfgsdgferen dead","5",false,false),
            MovieDetails(42,"poster link","Anime","insidious","2022","kids","G","200","Hololive","ersdfsdfsdfsdfen dead","5",false,false),
            MovieDetails(52,"poster link","Anime","hahahahaha","2212","horror","18+","12 episodes","YOASOBI","eresdfsdfsdfsdfn dead","5",false,false),
            MovieDetails(13,"poster link","Anime","Oshi no ko","2112","kids","G","200","Hololive","eren dead","5",false,false),
            MovieDetails(23,"poster link","Anime","kanojo mo kanojo","2312","horror","18+","200","Hololive","eren 3422344232dead","5",false,false),
            MovieDetails(33,"poster link","Anime","genshit impact","2412","kids","G","200","YOASOBI","eren432342342 dead","5",false,false),
            MovieDetails(43,"poster link","Anime","insidious","2052","horror","G","12 episodes","Hololive","ereredw43w4n dead","5",false,false),
            MovieDetails(53,"poster link","Anime","hahahahaha","2066","kids","ALL","200","YOASOBI","eren w43w43w43dead","5",false,false),
            MovieDetails(14,"poster link","Anime","Oshi no ko","2055","horror","G","200","Hololive","eren dew43w43w4ad","5",false,false),
            MovieDetails(24,"poster link","Anime","kanojo mo kanojo","2333","kids","ALL","200","YOASOBI","erenw34w43w34 dead","5",false,false),
            MovieDetails(34,"poster link","Anime","genshit impact","2212","horror","G","200","Hololive","eren dew34w34w34ad","5",false,false),
            MovieDetails(44,"poster link","Anime","insidious","2012","kids","ALL","200","YOASOBI","w34w34w34eren dead","5",false,false),
            MovieDetails(54,"poster link","Anime","hahahahaha","2011","adults","G","200","Hololive","erew34w343w4w34n dead","5",false,false),
        )
    }
}