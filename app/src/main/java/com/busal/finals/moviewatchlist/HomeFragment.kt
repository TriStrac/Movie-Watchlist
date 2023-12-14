package com.busal.finals.moviewatchlist

import android.content.Context
import android.os.Bundle
import android.util.Log
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

        binding.addMovieButton.setOnClickListener {
            for (movieDetails in movieList) {
                Log.d("MovieDetails", movieDetails.toString())
            }
        }

        if(LocalStorage(requireContext()).isSaved){
            initializeMovies()
            LocalStorage(requireContext()).isSaved=false
            saveToLocal()
        }
        displayMovies()
        return binding.root
    }
    override fun onListReloaded() {
        displayMovies()
        movieList = LocalStorage(requireContext()).movieList
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
            MovieDetails(1, "https://m.media-amazon.com/images/M/MV5BZmUwNGU0ODAtNGUxNy00ZjZmLTgzODgtMjQ2MWUyZjI4MmFkXkEyXkFqcGdeQXVyNjA5MDIyMzU@._V1_.jpg", "Anime", "Oshi no ko", "2023", "Drama", "PG", "12 episodes", "YOASOBI", "eren dead", "5", false, false),
            MovieDetails(2, "https://thenationroar.com/wp-content/uploads/2020/04/attack-on-titan-cast.jpg", "Anime", "Attack on Titan", "2013", "Action", "R", "25 episodes", "Wit Studio", "Fight for survival against titans", "4.5", false, false),
            MovieDetails(3, "https://i0.wp.com/wallpapercave.com/wp/wp7693752.jpg", "Anime", "My Hero Academia", "2016", "Action", "PG-13", "13 episodes", "Bones", "Superheroes in training", "4.8", false, false),
            MovieDetails(4, "https://image.tmdb.org/t/p/original/Sw50QpmJFUsWELy4z9QhTHFmas.jpg", "Anime", "Demon Slayer", "2019", "Action", "R", "26 episodes", "ufotable", "Tanjiro's journey to save his sister", "4.7", false, false),
            MovieDetails(5, "https://dailyanimeart.files.wordpress.com/2015/10/one-punch-man-poster.jpg", "Anime", "One Punch Man", "2015", "Action", "PG-13", "12 episodes", "Madhouse", "Saitama's quest for a challenging fight", "4.6", false, false),
            MovieDetails(6, "https://th.bing.com/th/id/R.c195b754307fae0a538500b4c9b38398?rik=ici%2fwOVE8EjDhA&riu=http%3a%2f%2fwww.impawards.com%2f2002%2fposters%2fspirited_away_xlg.jpg&ehk=uNNBDTzGq9TXFcjEKxAh5JonBZGpEvioAMhq%2bmM2lkQ%3d&risl=&pid=ImgRaw&r=0", "Anime", "Spirited Away", "2001", "Adventure", "PG", "1 hr 58 min", "Studio Ghibli", "Chihiro's magical journey in the spirit world", "4.9", false, false),
            MovieDetails(7, "https://th.bing.com/th/id/R.22faa5d6b1abb488503cbd8887baf84b?rik=jHI%2fDB5JNswXUQ&riu=http%3a%2f%2fscreencritix.com%2fwp-content%2fuploads%2f2013%2f10%2fdeath-note-poster.jpg&ehk=THUxplwPtT%2bxqZbr8lFinBYCP9Az50QaXvdrs9gfo00%3d&risl=&pid=ImgRaw&r=0", "Anime", "Death Note", "2006", "Mystery", "R", "37 episodes", "Madhouse", "Light Yagami's use of the Death Note", "4.8", false, false),
            MovieDetails(8, "https://image.tmdb.org/t/p/original/pZ7qsb8kqYqQuFzjFeAQ2IlbiXW.jpg", "Anime", "Your Name", "2016", "Drama", "PG", "1 hr 46 min", "CoMix Wave Films", "Taki and Mitsuha's body-swapping adventure", "4.9", false, false),
            MovieDetails(9, "https://image.tmdb.org/t/p/original/7zcIgfFGtHGyvS9tQhCFmjoMu14.jpg", "Anime", "Cowboy Bebop", "1998", "Sci-Fi", "R", "26 episodes", "Sunrise", "Bounty hunting in space with Spike Spiegel", "4.7", false, false),
            MovieDetails(10, "https://images.saymedia-content.com/.image/t_share/MTkzNTg0NzAyNDU5ODE1MzYz/the-21-saddest-anime-masterpiece-you-must-binge-watch.jpg", "Anime", "Fullmetal Alchemist: Brotherhood", "2009", "Fantasy", "R", "64 episodes", "Bones", "Edward and Alphonse Elric's quest for the Philosopher's Stone", "4.9", false, false)
        )
    }
}