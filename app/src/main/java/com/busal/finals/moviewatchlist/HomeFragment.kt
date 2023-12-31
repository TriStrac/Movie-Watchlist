package com.busal.finals.moviewatchlist

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.busal.finals.moviewatchlist.adapter.HomeListAdapter
import com.busal.finals.moviewatchlist.adapter.SearchListAdapter
import com.busal.finals.moviewatchlist.databinding.FragmentHomeBinding
import com.busal.finals.moviewatchlist.models.MovieDetails
import com.busal.finals.moviewatchlist.storage.LocalStorage
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Integer.min
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class HomeFragment : Fragment(), HomeListAdapter.ListReloadListener, SearchListAdapter.ListReloadListener {
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
    private lateinit var searchedMovieList:List<MovieDetails>
    private var isAddMovie = false
    private var toggleAnimeOrMovie = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.searchButton.setOnClickListener {
            binding.searchButton.visibility = View.INVISIBLE
            binding.addMovieButton.visibility = View.INVISIBLE
            binding.doneButton.visibility = View.VISIBLE
            binding.searchWatchListBarText.visibility = View.VISIBLE
        }

        binding.doneButton.setOnClickListener {
            binding.searchButton.visibility = View.VISIBLE
            binding.addMovieButton.visibility = View.VISIBLE
            binding.doneButton.visibility = View.INVISIBLE
            binding.searchWatchListBarText.visibility = View.INVISIBLE
        }

        binding.addMovieButton.setOnClickListener {
            if(isAddMovie){
                binding.addMovieButton.text = getString(R.string.add_movie)
                binding.searchButton.visibility = View.VISIBLE
                binding.animeAddButton.visibility = View.INVISIBLE
                binding.movieAddButton.visibility = View.INVISIBLE
                loadLocal()
                isListEmpty()
                binding.movieListRecyclerView.visibility = View.VISIBLE
                isAddMovie = false
            }else{
                binding.addMovieButton.text = getString(R.string.done_)
                binding.searchButton.visibility = View.INVISIBLE
                binding.animeAddButton.visibility = View.VISIBLE
                binding.movieAddButton.visibility = View.VISIBLE
                binding.movieListRecyclerView.visibility = View.INVISIBLE
                binding.addMoviesMessageText.visibility = View.INVISIBLE
                isAddMovie = true
            }
        }

        binding.animeAddButton.setOnClickListener {
            toggleAnimeOrMovie=true
            searchAnimeOrMovie()
        }
        binding.movieAddButton.setOnClickListener {
            toggleAnimeOrMovie=false
            searchAnimeOrMovie()
        }
        binding.cancelSearchButton.setOnClickListener {
            binding.addSearchQueryText.visibility = View.INVISIBLE
            binding.cancelSearchButton.visibility = View.INVISIBLE
            binding.proceedSearchButton.visibility = View.INVISIBLE
            binding.movieListRecyclerView.visibility = View.INVISIBLE
            binding.animeAddButton.visibility = View.VISIBLE
            binding.movieAddButton.visibility = View.VISIBLE
            binding.addMovieButton.visibility = View.VISIBLE
        }

        binding.proceedSearchButton.setOnClickListener {
            lifecycleScope.launch {
                queryApiSearch()
            }
        }

        loadLocal()
        isListEmpty()

        return binding.root
    }

    private suspend fun queryApiSearch() {
        val query = binding.addSearchQueryText.text.toString().trim()
        if (query.isNotEmpty()) {
            if (toggleAnimeOrMovie) {
                lifecycleScope.launch {
                    val result = animeSearch(query)
                    handleAnimeSearchResult(result)
                    getSearchedFromLocal()
                    filterSearched()
                    getSearchedFromLocal()
                    displaySearchResults()
                    binding.movieListRecyclerView.visibility = View.VISIBLE
                }
            }else{
                val result = movieSearch(query)
                handleMovieSearchResult(result)
                getSearchedFromLocal()
                filterSearched()
                getSearchedFromLocal()
                displaySearchResults()
                binding.movieListRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun getSearchedFromLocal(){
        searchedMovieList = LocalStorage(requireContext()).searchedMovieList
    }
    private fun displaySearchResults(){
        binding.movieListRecyclerView.adapter = null
        binding.movieListRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.movieListRecyclerView.adapter = SearchListAdapter(requireActivity(),searchedMovieList,this)
    }

    private fun filterSearched() {
        val localMovieList = LocalStorage(requireContext()).movieList
        val localMovieIds = localMovieList.map { it.id } ?: emptyList()
        val filteredMovieList = searchedMovieList.filter { movie ->
            movie.id !in localMovieIds
        }
        LocalStorage(requireContext()).searchedMovieList = filteredMovieList
    }

    private suspend fun movieSearch(query: String): String {
        val apiKey = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2N2RmYzlhZjJiYWM2MmY5NjA5NjZmNDEyNmFjZGY4ZiIsInN1YiI6IjY1NzFiNDFhYjA0NjA1MDExZDcyN2VlZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.aG-psNKl0wZcaRGqtHBB41GDkIxgn7EZlhwjeOY18_E"
        val apiUrl = "https://api.themoviedb.org/3/search/movie?query=${URLEncoder.encode(query, "UTF-8")}&include_adult=false&language=en-US&page=1"
        val authHeader = "Bearer $apiKey"

        return withContext(Dispatchers.IO) {
            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection

                // Set authorization header
                connection.setRequestProperty("Authorization", authHeader)

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()

                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                reader.close()
                inputStream.close()

                return@withContext response.toString()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return@withContext ""
        }
    }
    private fun handleMovieSearchResult(result: String) {
        try {
            val jsonResponse = JSONObject(result)

            // Check if "results" key exists
            if (jsonResponse.has("results")) {
                val resultsArray = jsonResponse.getJSONArray("results")

                // Create a list to store MovieDetails objects
                val searchedMovieListPrivate = mutableListOf<MovieDetails>()

                for (i in 0 until resultsArray.length()) {
                    val movieObject = resultsArray.getJSONObject(i)

                    val movieId = movieObject.getInt("id")
                    val title = movieObject.getString("title")
                    val releaseDate = movieObject.getString("release_date").substring(0, 4)
                    val genresArray = movieObject.getJSONArray("genre_ids")
                    val genres = mutableListOf<String>()

                    for (j in 0 until min(2, genresArray.length())) {
                        val genreId = genresArray.getInt(j)
                        genres.add(getGenreNameById(genreId))
                    }

                    val posterPath = movieObject.getString("poster_path")
                    val posterLink = "https://image.tmdb.org/t/p/w500$posterPath"
                    val overview = movieObject.getString("overview")
                    val popularity = movieObject.getDouble("popularity").toString()

                    val movieDetails = MovieDetails(
                        movieId,
                        posterLink,
                        "Movie",
                        title,
                        releaseDate,
                        genres.joinToString(),
                        popularity,
                        "",
                        "",
                        overview,
                        "",
                        isWatched = false, isRemoved = false
                    )
                    searchedMovieListPrivate.add(movieDetails)
                }

                LocalStorage(requireContext()).searchedMovieList = searchedMovieListPrivate
            } else {
                Log.w("MovieSearchTask", "No 'results' key found in the JSON response")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun getGenreNameById(genreId: Int): String {
        val genreMap = mapOf(
            28 to "Action",
            12 to "Adventure",
            16 to "Animation",
            35 to "Comedy",
            80 to "Crime",
            99 to "Documentary",
            18 to "Drama",
            10751 to "Family",
            14 to "Fantasy",
            36 to "History",
            27 to "Horror",
            10402 to "Music",
            9648 to "Mystery",
            10749 to "Romance",
            878 to "Science Fiction",
            10770 to "TV Movie",
            53 to "Thriller",
            10752 to "War",
            37 to "Western"
        )

        return genreMap.getOrDefault(genreId, "Unknown Genre")
    }

    private suspend fun animeSearch(query: String): String {
        val apiUrl = "https://api.jikan.moe/v4/anime?q=${query}"
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()

                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                reader.close()
                inputStream.close()

                return@withContext response.toString()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return@withContext ""
        }
    }
    private fun handleAnimeSearchResult(result: String) {
        try {
            val jsonResponse = JSONObject(result)

            // Check if "data" key exists
            if (jsonResponse.has("data")) {
                val dataArray = jsonResponse.getJSONArray("data")

                // Create a list to store MovieDetails objects
                val searchedMovieListPrivate = mutableListOf<MovieDetails>()

                for (i in 0 until dataArray.length()) {
                    val animeObject = dataArray.getJSONObject(i)
                    val malId = animeObject.getInt("mal_id")
                    val title = animeObject.getString("title")

                    val airedObject = animeObject.getJSONObject("aired")
                    val airedPropObject = airedObject.getJSONObject("prop")
                    val airedFromObject = airedPropObject.getJSONObject("from")

                    val genresArray = animeObject.getJSONArray("genres")
                    val genres = mutableListOf<String>()
                    for (j in 0 until genresArray.length()) {
                        val genreObject = genresArray.getJSONObject(j)
                        val genreName = genreObject.getString("name")
                        genres.add(genreName)
                    }



                    val imagesObject = animeObject.getJSONObject("images")
                    val posterLink = imagesObject.getJSONObject("jpg").getString("large_image_url")
                    val synopsis = animeObject.getString("synopsis")
                    val episodes = animeObject.getString("episodes")
                    val duration = animeObject.getString("duration")
                    val newDuration = "$episodes episodes, $duration"

                    val studiosArray = animeObject.getJSONArray("studios")
                    val studio = mutableListOf<String>()
                    for (j in 0 until studiosArray.length()) {
                        val studioObject = studiosArray.getJSONObject(j)
                        val studioName = studioObject.getString("name")
                        studio.add(studioName)
                    }

                    val releaseYear = airedFromObject.getString("year")
                    val rating = animeObject.getString("rating")

                    val movieDetails = MovieDetails(
                        malId,
                        posterLink,
                        "Anime",
                        title,
                        releaseYear,
                        genres.joinToString(),
                        rating,
                        newDuration,
                        "Studio: ${studio.joinToString()}",
                        synopsis,
                        "",
                        isWatched = false, isRemoved = false
                    )
                    searchedMovieListPrivate.add(movieDetails)
                }

                LocalStorage(requireContext()).searchedMovieList = searchedMovieListPrivate
            } else {
                Log.w("AnimeSearchTask", "No 'data' key found in the JSON response")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun searchAnimeOrMovie(){
        binding.addSearchQueryText.visibility = View.VISIBLE
        binding.cancelSearchButton.visibility = View.VISIBLE
        binding.proceedSearchButton.visibility = View.VISIBLE
        binding.animeAddButton.visibility = View.INVISIBLE
        binding.movieAddButton.visibility = View.INVISIBLE
        binding.addMovieButton.visibility = View.INVISIBLE
        if(toggleAnimeOrMovie){
            binding.addSearchQueryText.hint = "Search Anime.."
        }else{
            binding.addSearchQueryText.hint = "Search Movie.."
        }
    }
    override fun onListReloaded(source:String) {
        when (source) {
            "HomeListAdapter" -> {
                displayMovies()
                loadLocal()
                isListEmpty()
            }
            "SearchListAdapter" -> {
                filterSearched()
                getSearchedFromLocal()
                displaySearchResults()

            }
        }
    }

    private fun isListEmpty(){
        if(movieList.isNotEmpty()){
            displayMovies()
            if(listToDisplay.isEmpty()){
                binding.addMoviesMessageText.visibility = View.VISIBLE
            }else{
                binding.addMoviesMessageText.visibility = View.INVISIBLE
            }
        }else{
            binding.addMoviesMessageText.visibility = View.VISIBLE
        }
    }

    private lateinit var listToDisplay:List<MovieDetails>
    private fun filterDisplay(){
        binding.movieListRecyclerView.adapter = null
        listToDisplay = LocalStorage(requireContext()).movieList
        listToDisplay = listToDisplay.filter { !it.isRemoved && !it.isWatched }
    }
    private fun displayMovies(){
        filterDisplay()
        binding.movieListRecyclerView.adapter = null
        binding.movieListRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.movieListRecyclerView.adapter = HomeListAdapter(requireActivity(), listToDisplay,this)
    }
    private fun loadLocal(){
        movieList=LocalStorage(requireContext()).movieList
    }


}